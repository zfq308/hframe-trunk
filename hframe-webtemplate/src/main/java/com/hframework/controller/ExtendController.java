package com.hframework.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.hframe.domain.model.*;
import com.hframe.service.interfaces.*;
import com.hframework.base.service.CommonDataService;
import com.hframework.base.service.DataSetLoaderService;
import com.hframework.base.service.ModelLoaderService;
import com.hframework.beans.controller.ResultCode;
import com.hframework.beans.controller.ResultData;
import com.hframework.common.ext.CollectionUtils;
import com.hframework.common.ext.Grouper;
import com.hframework.common.ext.Mapper;
import com.hframework.common.frame.ServiceFactory;
import com.hframework.common.frame.cache.PropertyConfigurerUtils;
import com.hframework.common.util.*;
import com.hframework.common.util.message.VelocityUtil;
import com.hframework.common.util.message.XmlUtils;
import com.hframework.ext.datasoucce.DataSourceContextHolder;
import com.hframework.generator.util.CreatorUtil;
import com.hframework.generator.web.BaseGeneratorUtil;
import com.hframework.generator.web.bean.HfClassContainer;
import com.hframework.generator.web.bean.HfClassContainerUtil;
import com.hframework.generator.web.bean.HfModelContainer;
import com.hframework.generator.web.bean.HfModelContainerUtil;
import com.hframework.generator.web.mybatis.MyBatisGeneratorUtil;
import com.hframework.generator.web.sql.HfModelService;
import com.hframework.generator.web.sql.SqlGeneratorUtil;
import com.hframework.generator.web.sql.reverse.SQLParseUtil;
import com.hframework.web.ControllerHelper;
import com.hframework.web.bean.WebContext;
import com.hframework.web.bean.WebContextHelper;
import com.hframework.web.config.bean.DataSetHelper;
import com.hframework.web.config.bean.Module;
import com.hframework.web.config.bean.Program;
import com.hframework.web.config.bean.dataset.Entity;
import com.hframework.web.config.bean.module.Component;
import com.hframework.web.config.bean.module.Page;
import com.hframework.web.config.bean.program.Modules;
import com.hframework.web.config.bean.program.SuperManager;
import com.hframework.web.config.bean.program.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * User: zhangqh6
 * Date: 2016/5/11 0:16:16
 */
@Controller
@RequestMapping(value = "/extend")
public class ExtendController {
    private static final Logger logger = LoggerFactory.getLogger(ExtendController.class);

    @Resource
    private DataSetLoaderService dataSetLoaderService;

    @Resource
    private ModelLoaderService modelLoaderService;
    @Resource
    private CommonDataService commonDataService;

    @Resource
    private IHfpmProgramSV hfpmProgramSV;

    @Resource
    private IHfsecMenuSV hfsecMenuSV;

    @Resource
    private IHfsecUserSV hfsecUserSV;


    @Resource
    private IHfpmModuleSV hfpmModuleSV;

    @Resource
    private IHfcfgDbConnectSV iHfcfgDbConnectSV;
    @Resource
    private IHfpmProgramCfgSV iHfpmProgramCfgSV;


    @Resource
    private IHfsecMenuSV iHfsecMenuSV;

    @Resource
    private IHfmdEntitySV iHfmdEntitySV;

    @Resource
    private IHfmdEntityAttrSV hfmdEntityAttrSV;

    public enum EntityPageSet{

        SINGLE_MGR("_mgr","管理","qlist",new String[]{"qForm"}),
        SINGLE_CREATE("_create","创建","create",new String[]{}),
        SINGLE_EDIT("_edit","修改","edit",new String[]{}),
        SINGLE_DETAIL("_detail","明细","edit",new String[]{}),

        COMPLEX_MGR("_mgr","管理","qlist",new String[]{"qForm"}),
        COMPLEX_CREATE("_create","添加","cComb",new String[]{"cForm","cList"}),
        COMPLEX_EDIT("_edit","修改","eComb",new String[]{"eForm","eList"}),
        COMPLEX_DETAIL("_detail","查看","dComb",new String[]{"dForm","qList"});


        String id;
         String name;
         String pageTemplate;
         String[] component;

        EntityPageSet(String id, String name, String pageTemplate, String[] component) {
            this.id = id;
            this.name = name;
            this.pageTemplate = pageTemplate;
            this.component = component;
        }
    }

    /**
     * 重新加载（全量）
     * @return
     */
    @RequestMapping(value = "/model_diff.json")
    @ResponseBody
    public ResultData getModelDiff(HttpServletRequest request){
        logger.debug("request : {}");
        try{
            Map<String, String>  pageContextParams = DefaultController.getPageContextParams(request);
            WebContext.putContext(DefaultController.getPageContextRealyParams(pageContextParams));
            Map<String, String> pageFlowParams = WebContext.get(HashMap.class.getName());

            String companyCode = "hframe";
            String programCode = "hframe";
            String programeName = "框架";
            String moduleCode = "hframe";
            String moduleName = "框架";
            if(pageFlowParams != null) {
                if(pageFlowParams.containsKey("hfpmProgramId") && StringUtils.isNotBlank(pageFlowParams.get("hfpmProgramId"))) {
                    HfpmProgram program = hfpmProgramSV.getHfpmProgramByPK(Long.parseLong(pageFlowParams.get("hfpmProgramId")));
                    programCode = program.getHfpmProgramCode();
                    programeName = program.getHfpmProgramName();
                }
                if(pageFlowParams.containsKey("hfpmModuleId") && StringUtils.isNotBlank(pageFlowParams.get("hfpmModuleId"))) {
                    HfpmModule module = hfpmModuleSV.getHfpmModuleByPK(Long.parseLong(pageFlowParams.get("hfpmModuleId")));
                    moduleCode = module.getHfpmModuleCode();
                    moduleName = module.getHfpmModuleName();
                }
            }



//            final String hfcfgDbConnectId = request.getParameter("hfcfgDbConnectId");



            String  configSqlPath= modelLoaderService.load(companyCode, programCode, programeName, moduleCode, moduleName);

            HfModelContainer targetModelContainer = SQLParseUtil.parseModelContainerFromSQLFile(
                    configSqlPath, programCode, programeName, moduleCode, moduleName);


            startDynamicDataSource(pageFlowParams);
            String dbSqlPath = SqlGeneratorUtil.createSqlFile(companyCode, programCode);
            DataSourceContextHolder.clear();

            HfModelContainer curDbModelContainer = SQLParseUtil.parseModelContainerFromSQLFile(
                    dbSqlPath, programCode, programeName, moduleCode, moduleName);

            final HfModelContainer[] resultModelContainers =
                    HfModelContainerUtil.mergerModelContainer(curDbModelContainer, targetModelContainer);
            final List<String> result = HfModelContainerUtil.getSql(resultModelContainers[0], resultModelContainers[1]);
            final List<Map<String, String>> sqls = new ArrayList<Map<String, String>>();
            for (final String sql : result) {
                sqls.add(new HashMap<String, String>(){{
                        put("sql",sql.replaceAll("\n", ""));
                }});
            }

            generateDefaultDataSetIfNotExists(resultModelContainers, programCode, programeName, moduleCode, moduleName);

//            WebContext.putContext("hfcfgDbConnectId", hfcfgDbConnectId);
            return ResultData.success(new HashMap<String,Object>(){{
                put("NewEntityId", new HashMap<String, Object>() {{
                    put("list", Lists.newArrayList(resultModelContainers[0].getEntityMap().values()));
                }});
                put("NewEntityAttrId", new HashMap<String, Object>() {{
                    put("list", Lists.newArrayList(resultModelContainers[0].getEntityAttrMap().values()));
                }});
                put("ModEntityId", new HashMap<String, Object>() {{
                    put("list", Lists.newArrayList(resultModelContainers[1].getEntityMap().values()));
                }});
                put("ModEntityAttrId", new HashMap<String, Object>() {{
                    put("list", Lists.newArrayList(resultModelContainers[1].getEntityAttrMap().values()));
                }});
                put("SelectDbConnector", new HashMap<String, Object>() {{
                    put("data", new HashMap(){{
                        put("hfcfgDbConnectId",null);
                    }});
                }});


                put("sql", new HashMap<String, Object>() {{
                    put("list", sqls);
                }});
            }});
        }catch (Exception e) {
            logger.error("error : ", e);
            return ResultData.error(ResultCode.ERROR);
        }finally {
            DataSourceContextHolder.clear();
        }
    }

    private HfcfgDbConnect startDynamicDataSource(Map<String, String> pageFlowParams) throws Exception {
        HfpmProgramCfg hfpmProgramCfg = new HfpmProgramCfg();
        ReflectUtils.setFieldValue(hfpmProgramCfg, pageFlowParams);
        HfpmProgramCfg_Example example = ExampleUtils.parseExample(hfpmProgramCfg, HfpmProgramCfg_Example.class);
        List<HfpmProgramCfg> programCfgList = iHfpmProgramCfgSV.getHfpmProgramCfgListByExample(example);
        if(programCfgList != null && programCfgList.size() == 1) {
            HfpmProgramCfg programCfg = programCfgList.get(0);
            Long hfcfgDbConnectId1 = programCfg.getHfcfgDbConnectId();
            if(hfcfgDbConnectId1 != null) {
                HfcfgDbConnect dbConnect = iHfcfgDbConnectSV.getHfcfgDbConnectByPK(hfcfgDbConnectId1);
                String url = dbConnect.getUrl();
                String user = dbConnect.getUser();
                String password = dbConnect.getPassword();
                DataSourceContextHolder.setDbInfo(url,user,password);
                return dbConnect;
            }

        }

        return null;
    }

    private void generateDefaultDataSetIfNotExists(HfModelContainer[] resultModelContainers, String programCode, String programName, String moduleCode, String moduleName) throws Exception {

        HfModelContainer dbModelContainer = HfModelService.get().getModelContainerFromDB(
                programCode, programName, moduleCode, moduleName);

        resultModelContainers =
                HfModelContainerUtil.mergerEntityToDataSetReturnOnly(resultModelContainers,dbModelContainer);
        System.out.println(resultModelContainers);

        HfModelService.get().executeModelInsert(resultModelContainers[0]);
//        HfModelService.get().executeModelUpdate(resultModelContainers[1]);
    }

    /**
     * menuChart初始化处理
     * @return
     */
    @RequestMapping(value = "/menu_chart.json")
    @ResponseBody
    public ResultData getMenuChart(HttpServletRequest request){
        logger.debug("request : {}");
        try{
            Map<String, String>  pageContextParams = DefaultController.getPageContextParams(request);
            WebContext.putContext(DefaultController.getPageContextRealyParams(pageContextParams));
            Map<String, String> pageFlowParams = WebContext.get(HashMap.class.getName());

            String companyCode = "hframe";
            String programCode = "hframe";
            String programeName = "框架";
            String moduleCode = "hframe";
            String moduleName = "框架";
            if(pageFlowParams != null) {
                if(pageFlowParams.containsKey("hfpmProgramId") && StringUtils.isNotBlank(pageFlowParams.get("hfpmProgramId"))) {
                    HfpmProgram program = hfpmProgramSV.getHfpmProgramByPK(Long.parseLong(pageFlowParams.get("hfpmProgramId")));
                    programCode = program.getHfpmProgramCode();
                    programeName = program.getHfpmProgramName();
                }
                if(pageFlowParams.containsKey("hfpmModuleId") && StringUtils.isNotBlank(pageFlowParams.get("hfpmModuleId"))) {
                    HfpmModule module = hfpmModuleSV.getHfpmModuleByPK(Long.parseLong(pageFlowParams.get("hfpmModuleId")));
                    moduleCode = module.getHfpmModuleCode();
                    moduleName = module.getHfpmModuleName();
                }
            }

            startDynamicDataSource(pageFlowParams);
            HfsecMenu_Example hfsecMenuExample = new HfsecMenu_Example();
            HfsecMenu hfsecMenu = new HfsecMenu();
            hfsecMenu.setParentHfsecMenuId(-1L);
            final Map<Long, List<HfsecMenu>> result = hfsecMenuSV.getHfsecMenuTreeByParentId(hfsecMenu, hfsecMenuExample);

            HfsecMenu  virtualNode = new HfsecMenu();
            virtualNode.setParentHfsecMenuId(-1L);
            virtualNode.setHfsecMenuId(-2L);
            virtualNode.setHfsecMenuName("未设置菜单");
            if(!result.containsKey(-1L)) result.put(-1L, new ArrayList<HfsecMenu>());
            result.get(-1L).add(virtualNode);

            hfsecMenuExample = new HfsecMenu_Example();
            hfsecMenu.setParentHfsecMenuId(-2L);
            Map<Long, List<HfsecMenu>> virtualMenus = hfsecMenuSV.getHfsecMenuTreeByParentId(hfsecMenu, hfsecMenuExample);
            if(virtualMenus.containsKey(-2L)) {

                result.putAll(virtualMenus);
            }

            return ResultData.success(new HashMap<String,Object>(){{
                put("AllMenuTree",result);
            }});
        }catch (Exception e) {
            logger.error("error : ", e);
            return ResultData.error(ResultCode.ERROR);
        }finally {
            DataSourceContextHolder.clear();
        }
    }


    /**
     * 数据保存
     * @return
     */
    @RequestMapping(value = "/save_menu.json")
    @ResponseBody
    public ResultData saveData(HttpServletRequest request,
                               HttpServletResponse response){

        try {
            Map<String, String>  pageContextParams = DefaultController.getPageContextParams(request);
            WebContext.putContext(DefaultController.getPageContextRealyParams(pageContextParams));
            Map<String, String> pageFlowParams = WebContext.get(HashMap.class.getName());
            startDynamicDataSource(pageFlowParams);
            ResultData resultData = new DefaultController().saveData(request, response);
            return resultData;
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            DataSourceContextHolder.clear();
        }

        return ResultData.error(ResultCode.UNKNOW);
    }


    /**
     * 重新加载（全量）
     * @return
     */
    @RequestMapping(value = "/model_execute.json")
    @ResponseBody
    public ResultData modelExecute(@RequestParam(value="checkIds[]",required=false) String[] sqls, HttpServletRequest request){
        if(sqls != null && sqls.length > 0 && !sqls[0].endsWith(";")) {
            sqls = new String[]{Joiner.on(",").join(sqls)};
        }
        logger.debug("request : {}", sqls);
        try{

            Map<String, String>  pageContextParams = DefaultController.getPageContextParams(request);
            WebContext.putContext(DefaultController.getPageContextRealyParams(pageContextParams));
            Map<String, String> pageFlowParams = WebContext.get(HashMap.class.getName());
            startDynamicDataSource(pageFlowParams);
            commonDataService.executeDBStructChange(Arrays.asList(sqls));
            return ResultData.success();
        }catch (Exception e) {
            logger.error("error : ", e);
            return ResultData.error(ResultCode.ERROR);
        }finally {
            DataSourceContextHolder.clear();
        }
    }

    /**
     * 重新加载（全量）
     * @return
     */
    @RequestMapping(value = "/code_generate.json")
    @ResponseBody
    public ResultData codeGenerate(@RequestParam(value="checkIds[]",required=false) Set<String> entityCodes, HttpServletRequest request){
        logger.debug("request : {}", entityCodes);

        try{

            Map<String, String>  pageContextParams = DefaultController.getPageContextParams(request);
            WebContext.putContext(DefaultController.getPageContextRealyParams(pageContextParams));
            Map<String, String> pageFlowParams = WebContext.get(HashMap.class.getName());

            String companyCode = "hframe";
            String programCode = "hframe";
            Long programId = -1L;
            String programName = "框架";
            String moduleCode = "hframe";
            String moduleName = "框架";
            if(pageFlowParams != null) {
                if(pageFlowParams.containsKey("hfpmProgramId") && StringUtils.isNotBlank(pageFlowParams.get("hfpmProgramId"))) {
                    HfpmProgram program = hfpmProgramSV.getHfpmProgramByPK(Long.parseLong(pageFlowParams.get("hfpmProgramId")));
                    programId  = program.getHfpmProgramId();
                    programCode = program.getHfpmProgramCode();
                    programName = program.getHfpmProgramName();
                }
                if(pageFlowParams.containsKey("hfpmModuleId") && StringUtils.isNotBlank(pageFlowParams.get("hfpmModuleId"))) {
                    HfpmModule module = hfpmModuleSV.getHfpmModuleByPK(Long.parseLong(pageFlowParams.get("hfpmModuleId")));
                    moduleCode = module.getHfpmModuleCode();
                    moduleName = module.getHfpmModuleName();
                }
            }

            startDynamicDataSource(pageFlowParams);
            DataSourceContextHolder.DataSourceDescriptor dataSourceInfo = DataSourceContextHolder.getDBInfoAnyMore();
            String sql = SqlGeneratorUtil.getSqlContent(entityCodes);
            DataSourceContextHolder.clear();

            final HfModelContainer hfModelContainer = SQLParseUtil.parseModelContainerFromSQL(sql, null, null, null, null);
            HfModelContainer configModelContainer = HfModelService.get().getModelContainerFromDB(
                    programCode, programName, moduleCode, moduleName);
            List<Map<String, String>> tables = new ArrayList<Map<String, String>>();
            for (final String tableName : entityCodes) {
                final HfmdEntity entity = configModelContainer.getEntity(tableName);
                HfmdEntityAttr keyAttrInfo = configModelContainer.getEntityAttr(tableName, tableName + "_id");
                Long hfmdEntityAttrId = keyAttrInfo.getHfmdEntityAttrId();

                HfmdEntityAttr_Example example = new HfmdEntityAttr_Example();
                example.createCriteria().andRelHfmdEntityAttrIdEqualTo(hfmdEntityAttrId).andHfmdEntityIdEqualTo(entity.getHfmdEntityId());
                List<HfmdEntityAttr> hfmdEntityAttrListByExample = hfmdEntityAttrSV.getHfmdEntityAttrListByExample(example);

                HashMap<String, String> hashMap = new HashMap<String, String>() {{
                    put("tableName", tableName);
                    put("tableDesc", entity.getHfmdEntityName());
                    put("generatedKey", tableName + "_id");

                }};
                if(hfmdEntityAttrListByExample != null && hfmdEntityAttrListByExample.size() > 0) {
                    hashMap.put("parentId", hfmdEntityAttrListByExample.get(0).getHfmdEntityAttrCode());
                }
                tables.add(hashMap);
            }

            String projectBasePath = CreatorUtil.getTargetProjectBasePath(companyCode,
                    "hframe".equals(programCode) ? "trunk" : programCode, moduleCode);

            String projectRootPath = projectBasePath + "\\hframe-core";

            String mybatisConfigFilePath = BaseGeneratorUtil.generateMybatisConfig(tables, projectRootPath, programCode, dataSourceInfo);
            MyBatisGeneratorUtil.generate(new File(mybatisConfigFilePath));
            BaseGeneratorUtil.generator(mybatisConfigFilePath, companyCode, programCode, moduleCode);

            ShellExecutor.exeCmd(projectBasePath + "/build/compile.bat");

            return ResultData.success();
        }catch (Exception e) {
            logger.error("error : ", e);
            return ResultData.error(ResultCode.ERROR);
        }
    }


    /**
     * 页面生成
     * @return
     */
    @RequestMapping(value = "/page_generate.json")
    @ResponseBody
    public ResultData pageGenerate(@RequestParam(value="dataIds[]",required=false) List<String> entityCodes, String moduleCode, HttpServletRequest request){
        logger.debug("request : {}", entityCodes, moduleCode);
        try{

            Map<String, String>  pageContextParams = DefaultController.getPageContextParams(request);
            WebContext.putContext(DefaultController.getPageContextRealyParams(pageContextParams));
            Map<String, String> pageFlowParams = WebContext.get(HashMap.class.getName());

            String companyCode = "hframe";
            Long programId = null;
            String programCode = "hframe";
            String programName = "框架";
            Long moduleId = null;
            String moduleName = "框架";
            if(pageFlowParams != null) {
                if(pageFlowParams.containsKey("hfpmProgramId") && StringUtils.isNotBlank(pageFlowParams.get("hfpmProgramId"))) {
                    HfpmProgram program = hfpmProgramSV.getHfpmProgramByPK(Long.parseLong(pageFlowParams.get("hfpmProgramId")));
                    programId = program.getHfpmProgramId();
                    programCode = program.getHfpmProgramCode();
                    programName = program.getHfpmProgramName();
                }
//                if(pageFlowParams.containsKey("hfpmModuleId") && StringUtils.isNotBlank(pageFlowParams.get("hfpmModuleId"))) {
//                    HfpmModule module = hfpmModuleSV.getHfpmModuleByPK(Long.parseLong(pageFlowParams.get("hfpmModuleId")));
//                    moduleCode = module.getHfpmModuleCode();
//                    moduleName = module.getHfpmModuleName();
//                }
            }

            HfmdEntity_Example example = new HfmdEntity_Example();
            HfmdEntity_Example.Criteria criteria = example.createCriteria();
            if(programId != null) criteria.andHfpmProgramIdEqualTo(programId);
            if(moduleId != null) criteria.andHfpmModuleIdEqualTo(moduleId);

            if(programId == null && moduleId == null) criteria.andHfmdEntityCodeLike("hf%");
            final List<HfmdEntity> hfmdEntityListByExample = iHfmdEntitySV.getHfmdEntityListByExample(example);

            Map<String, HfmdEntity> cache = CollectionUtils.convert(hfmdEntityListByExample, new Mapper<String, HfmdEntity>() {
                public <K> K getKey(HfmdEntity hfmdEntity) {
                    return (K) hfmdEntity.getHfmdEntityCode();
                }
            });

            Module module = new Module();
            module.setCode(moduleCode);
            List<Page> pageList = new ArrayList<Page>();
            module.setPageList(pageList);

            List<Page> menuList = new ArrayList<Page>();
            for (String entityCode : entityCodes) {
                if(StringUtils.isBlank(entityCode)) {
                    continue;
                }
                String[] lineEntityCodes = entityCode.substring(0,entityCode.length()-1).split("\\|");
                String rootEntityCode = lineEntityCodes[0];
                String rootEntityName = cache.get(rootEntityCode).getHfmdEntityName();

                String[] relEntityCodes = Arrays.copyOfRange(lineEntityCodes, 1, lineEntityCodes.length);

                List<Page> pages = getPages(moduleCode, rootEntityCode, rootEntityName, relEntityCodes);
                menuList.add(pages.get(0));
                pageList.addAll(pages);
            }

            WebContextHelper contextHelper = new WebContextHelper(companyCode, programCode,null, "default");
            String pageFilePath = contextHelper.programConfigRootDir + "/" + contextHelper.programConfigModuleDir + "/" + moduleCode + ".xml";

            String xml = XmlUtils.writeValueAsString(module);
//            String pageFilePath =  PropertyConfigurerUtils.getProperty(DataSetLoaderService.CreatorConst.PROJECT_BASE_FILE_PATH) +
//                    "/hframe-webtemplate/src/main/resources/program/hframe/module/" + moduleCode + ".xml";
            System.out.println(pageFilePath);
            System.out.println(xml);
            FileUtils.writeFile(pageFilePath, xml);


            dataSetLoaderService.load(request.getSession().getServletContext(),companyCode,programCode,moduleCode);

            startDynamicDataSource(pageFlowParams);
            List<HfsecMenu> hfsecMenuList = iHfsecMenuSV.getHfsecMenuListByExample(new HfsecMenu_Example());
            Iterator<Page> iterator = menuList.iterator();
            while(iterator.hasNext()) {
                Page page = iterator.next();
                for (HfsecMenu hfsecMenu : hfsecMenuList) {
                    if(page.getId().equals(hfsecMenu.getHfsecMenuCode())) {
                        iterator.remove();
                        break;
                    }
                }
            }

            for (Page page : menuList) {
                HfsecMenu hfsecMenu = new HfsecMenu();
                hfsecMenu.setHfsecMenuCode(page.getId());
                hfsecMenu.setHfsecMenuName(page.getName());
                hfsecMenu.setHfsecMenuDesc(page.getName());
                hfsecMenu.setMenuLevel(1);
                hfsecMenu.setParentHfsecMenuId(-2L);
                hfsecMenu.setIcon("");
                hfsecMenu.setUrl("/" + moduleCode + "/" + page.getId() + ".html");
                iHfsecMenuSV.create(hfsecMenu);
            }
            return ResultData.success();
        }catch (Exception e) {
            logger.error("error : ", e);

            return ResultData.error(ResultCode.ERROR);
        }finally {
            DataSourceContextHolder.clear();
        }
    }

    private List<Page> getPages(String moduleCode, String rootEntityCode, String rootEntityName, String[] relEntityCodes) {

        List<Page> pages = new ArrayList<Page>();


        if(relEntityCodes == null || relEntityCodes.length == 0) {
            pages.add(getPage(EntityPageSet.SINGLE_MGR, moduleCode, rootEntityCode, rootEntityName,null));
            pages.add(getPage(EntityPageSet.SINGLE_CREATE, moduleCode, rootEntityCode, rootEntityName,null));
            pages.add(getPage(EntityPageSet.SINGLE_EDIT, moduleCode, rootEntityCode, rootEntityName,null));
            pages.add(getPage(EntityPageSet.SINGLE_DETAIL, moduleCode, rootEntityCode, rootEntityName,null));

        }else {
            pages.add(getPage(EntityPageSet.COMPLEX_MGR, moduleCode, rootEntityCode, rootEntityName,relEntityCodes));
            pages.add(getPage(EntityPageSet.COMPLEX_CREATE, moduleCode, rootEntityCode, rootEntityName, relEntityCodes));
            pages.add(getPage(EntityPageSet.COMPLEX_EDIT, moduleCode, rootEntityCode, rootEntityName, relEntityCodes));
            pages.add(getPage(EntityPageSet.COMPLEX_DETAIL, moduleCode, rootEntityCode, rootEntityName, relEntityCodes));
        }

        return pages;
    }

    private Page getPage(EntityPageSet entityPage, String moduleCode, String rootEntityCode, String rootEntityName, String[] relEntityCodes) {
        Page page = new Page();

        page.setId(rootEntityCode + entityPage.id);
        page.setName(rootEntityName + entityPage.name);
        page.setPageTemplate(entityPage.pageTemplate);
        page.setDataSet(moduleCode + "/" + rootEntityCode);
        page.setComponentList(new ArrayList<Component>());
        String[] components = entityPage.component;
        for (String componentId : components) {
            Component component = new Component();
            component.setId(componentId);
            if(componentId.equals("qForm")) {
                component.setDataSet(moduleCode + "/" + rootEntityCode + "_DS4Q");
            }else if(componentId.equals("cList") && relEntityCodes != null) {
                component.setDataSet(moduleCode + "/" + relEntityCodes[0]);
            }else if(componentId.equals("eList") && relEntityCodes != null) {
                component.setDataSet(moduleCode + "/" + relEntityCodes[0]);
            }else if(componentId.equals("qList") && relEntityCodes != null) {
                component.setDataSet(moduleCode + "/" + relEntityCodes[0]);
            }else {
                component.setDataSet(moduleCode + "/" + rootEntityCode);
            }
            page.getComponentList().add(component);
        }

        return page;
    }


    /**
     * 代码差异比对
     * @return
     */
    @RequestMapping(value = "/code_diff.json")
    @ResponseBody
    public ResultData getCodeDiff(HttpServletRequest request){
        logger.debug("request : {}");
        try{
            Map<String, String>  pageContextParams = DefaultController.getPageContextParams(request);
            WebContext.putContext(DefaultController.getPageContextRealyParams(pageContextParams));
            Map<String, String> pageFlowParams = WebContext.get(HashMap.class.getName());

            String companyCode = "hframe";
            String programCode = "hframe";
            Long programId = -1L;
            String programName = "框架";
            String moduleCode = "hframe";
            String moduleName = "框架";
            HfpmProgram hfpmProgram = null;
            if(pageFlowParams != null) {
                if(pageFlowParams.containsKey("hfpmProgramId") && StringUtils.isNotBlank(pageFlowParams.get("hfpmProgramId"))) {
                    hfpmProgram = hfpmProgramSV.getHfpmProgramByPK(Long.parseLong(pageFlowParams.get("hfpmProgramId")));
                    programCode = hfpmProgram.getHfpmProgramCode();
                    programId = hfpmProgram.getHfpmProgramId();
                    programName = hfpmProgram.getHfpmProgramName();
                }
                if(pageFlowParams.containsKey("hfpmModuleId") && StringUtils.isNotBlank(pageFlowParams.get("hfpmModuleId"))) {
                    HfpmModule module = hfpmModuleSV.getHfpmModuleByPK(Long.parseLong(pageFlowParams.get("hfpmModuleId")));
                    moduleCode = module.getHfpmModuleCode();
                    moduleName = module.getHfpmModuleName();
                }
            }

            HfcfgDbConnect dataSourceInfo = startDynamicDataSource(pageFlowParams);
            String dbSqlPath = SqlGeneratorUtil.createSqlFile(companyCode, programCode);
            DataSourceContextHolder.clear();
            HfModelContainer targetFileModelContainer = SQLParseUtil.parseModelContainerFromSQLFile(
                    dbSqlPath, programCode, programName, moduleCode, moduleName);
            HfClassContainer targetClassContainer = HfClassContainerUtil.getClassInfoContainer(targetFileModelContainer);


            String projectBasePath = CreatorUtil.getTargetProjectBasePath(companyCode,
                    "hframe".equals(programCode) ? "trunk" : programCode, moduleCode);


            if(!new File(projectBasePath).exists()) {
                WebContextHelper helper = new WebContextHelper(companyCode,programCode,moduleCode,"");
                String programTemplatePath = CreatorUtil.getTargetProjectBasePath("hframe", "template", moduleCode);
                FileUtils.copyFolder(programTemplatePath, projectBasePath);

                FileUtils.copyFolder(helper.programConfigRootDir + "/" + helper.programConfigMapperDir.replaceAll(programCode,"template"),
                        helper.programConfigRootDir + "/" + helper.programConfigMapperDir);
                FileUtils.delFolder(helper.programConfigRootDir + "/" + helper.programConfigMapperDir.replaceAll(programCode,"template"));
                if(dataSourceInfo != null) {
                    Map map = new HashMap();
                    map.put("Jdbc", new BaseGeneratorUtil.Jdbc(dataSourceInfo.getUrl().replaceAll("&", "&amp;"), dataSourceInfo.getUser(), dataSourceInfo.getPassword()));
                    String content = VelocityUtil.produceTemplateContent("com/hframework/generator/vm/jdbcProperties.vm", map);
                    System.out.println(content);
                    FileUtils.writeFile(projectBasePath + "/hframe-core/src/main/resources/properties/dataSource.properties", content);
                }

                Map map = new HashMap();
                map.put("companyCode", companyCode);
                map.put("programCode", programCode);
                String content = VelocityUtil.produceTemplateContent("com/hframework/generator/vm/compileBat.vm", map);
                System.out.println(content);
                FileUtils.writeFile(projectBasePath + "/build/compile.bat", content);

                content = VelocityUtil.produceTemplateContent("com/hframework/generator/vm/compileSh.vm", map);
                System.out.println(content);
                FileUtils.writeFile(projectBasePath + "/build/compile.sh", content);


                Program program = new Program();
                program.setCompany(companyCode);
                program.setCode(programCode);
                program.setName(programName);
                program.setDescription(hfpmProgram != null ? hfpmProgram.getHfpmProgramDesc() : programName);


                Modules modules = new Modules();
                program.setModules(modules);

                List<com.hframework.web.config.bean.program.Module> moduleList = new ArrayList<com.hframework.web.config.bean.program.Module>();
                modules.setModuleList(moduleList);

                HfpmModule_Example example = new HfpmModule_Example();
                example.createCriteria().andHfpmProgramIdEqualTo(programId);
                List<HfpmModule> hfpmModuleListByExample = hfpmModuleSV.getHfpmModuleListByExample(example);
                for (HfpmModule module : hfpmModuleListByExample) {
                    com.hframework.web.config.bean.program.Module module1 = new com.hframework.web.config.bean.program.Module();
                    module1.setCode(module.getHfpmModuleCode());
                    module1.setName(module.getHfpmModuleName());
                    moduleList.add(module1);
                }

                Template template = new Template();
                template.setCode("default");
                template.setPath("hframework.template.default");
                program.setTemplate(template);

                program.setWelcome("uc/login.html");

                SuperManager superManager = new SuperManager();
                superManager.setCode("admin");
                superManager.setPassword("admin");
                superManager.setName("草鸡管理员");
                program.setSuperManager(superManager);
                String xml = XmlUtils.writeValueAsString(program);
                FileUtils.writeFile(projectBasePath + "/hframe-web/src/main/resources/program/program.xml", xml);

                startDynamicDataSource(pageFlowParams);
                HfsecUser hfsecUser = new HfsecUser();
                hfsecUser.setHfsecUserName(superManager.getName());
                hfsecUser.setAccount(superManager.getCode());
                hfsecUser.setPassword(superManager.getPassword());
                hfsecUser.setStatus(1);
                hfsecUser.setAvatar("http://pic.hanhande.com/files/141127/1283574_094432_8946.jpg");
                hfsecUserSV.create(hfsecUser);
                DataSourceContextHolder.clear();

                map = new HashMap();
                map.put("programName", programName);
                map.put("menuDataSet", "hframe/hfsec_menu");
                map.put("userDataSet", "hframe/hfsec_user");
                content = VelocityUtil.produceTemplateContent("com/hframework/generator/vm/defaultPage.vm", map);
                System.out.println(content);

                WebContextHelper contextHelper = new WebContextHelper(companyCode,programCode,moduleCode,"default");
                String pageFilePath = contextHelper.programConfigRootDir + "/" + contextHelper.programConfigModuleDir + "/default.xml";
                FileUtils.writeFile(pageFilePath, content);
            }


            String projectCompileTargetPath = projectBasePath + "\\hframe-core\\target\\classes\\";
//            String filePath = "/D:/my_workspace/hframe-trunk" + "\\hframe-core\\target\\classes\\";
            String classPackage = "com.hframe.domain.model.";
            HfClassContainer originClassContainer = HfClassContainerUtil.fromClassPath(
                    projectCompileTargetPath, classPackage, programCode, programName);

            final List<Map<String, String>>[] result = HfClassContainerUtil.compare(originClassContainer, targetClassContainer);

            return ResultData.success(new HashMap<String,Object>(){{
                put("AddClassInfo", new HashMap<String, Object>() {{
                    put("list", result[0]);
                }});
                put("ModClassInfo", new HashMap<String, Object>() {{
                    put("list", result[1]);
                }});
                put("DelClassInfo", new HashMap<String, Object>() {{
                    put("list", result[2]);
                }});
            }});
        }catch (Exception e) {
            logger.error("error : ", e);
            return ResultData.error(ResultCode.ERROR);
        }finally {
            DataSourceContextHolder.clear();
        }
    }

    /**
     * 代码差异比对
     * @return
     */
    @RequestMapping(value = "/page_load.json")
    @ResponseBody
    public ResultData getPageLoad(HttpServletRequest request){
        logger.debug("request : {}");
        try{
            Map<String, String>  pageContextParams = DefaultController.getPageContextParams(request);
            WebContext.putContext(DefaultController.getPageContextRealyParams(pageContextParams));
            Map<String, String> pageFlowParams = WebContext.get(HashMap.class.getName());

            String companyCode = "hframe";
            Long programId = null;
            String programCode = "hframe";
            String programName = "框架";
            Long moduleId = null;
            String moduleCode = "hframe";
            String moduleName = "框架";
            if(pageFlowParams != null) {
                if(pageFlowParams.containsKey("hfpmProgramId") && StringUtils.isNotBlank(pageFlowParams.get("hfpmProgramId"))) {
                    HfpmProgram program = hfpmProgramSV.getHfpmProgramByPK(Long.parseLong(pageFlowParams.get("hfpmProgramId")));
                    programId = program.getHfpmProgramId();
                    programCode = program.getHfpmProgramCode();
                    programName = program.getHfpmProgramName();
                }
                if(pageFlowParams.containsKey("hfpmModuleId") && StringUtils.isNotBlank(pageFlowParams.get("hfpmModuleId"))) {
                    HfpmModule module = hfpmModuleSV.getHfpmModuleByPK(Long.parseLong(pageFlowParams.get("hfpmModuleId")));
                    moduleId = module.getHfpmModuleId();
                    moduleCode = module.getHfpmModuleCode();
                    moduleName = module.getHfpmModuleName();

                }
            }
            HfmdEntity_Example example = new HfmdEntity_Example();
            HfmdEntity_Example.Criteria criteria = example.createCriteria();
            if(programId != null) criteria.andHfpmProgramIdEqualTo(programId);
            if(moduleId != null) criteria.andHfpmModuleIdEqualTo(moduleId);

            if(programId == null && moduleId == null) criteria.andHfmdEntityCodeLike("hf%");


            final List<HfmdEntity> hfmdEntityListByExample = iHfmdEntitySV.getHfmdEntityListByExample(example);

            Map<Module, List<List<Entity>>> entityRelats = WebContext.get(companyCode,programCode,"default").getEntityRelats();
            final Map<Module, List<List<HfmdEntity>>> todoList = new HashMap<Module, List<List<HfmdEntity>>>();
            for (Module module : entityRelats.keySet()) {
                todoList.put(module, new ArrayList<List<HfmdEntity>>());
                List<List<HfmdEntity>> targetList = todoList.get(module);
                List<List<Entity>> originList = entityRelats.get(module);
                for (List<Entity> entities : originList) {
                    List<HfmdEntity> target = new ArrayList<HfmdEntity>();
                    for (Entity entity : entities) {
                        if(StringUtils.isNotBlank(entity.getText())) {
                            target.add(getHfmdEntity(hfmdEntityListByExample, entity));
                        }
                    }
                    targetList.add(target);
                }
            }


            System.out.println("==>" + JSON.toJSONString(entityRelats));
            return ResultData.success(new HashMap<String,Object>(){{
                put("TodoList", hfmdEntityListByExample);
                put("DownList", todoList);
            }});
        }catch (Exception e) {
            logger.error("error : ", e);
            return ResultData.error(ResultCode.ERROR);
        }
    }

    private HfmdEntity getHfmdEntity(List<HfmdEntity> hfmdEntityListByExample, Entity entity) {
        Iterator<HfmdEntity> iterator = hfmdEntityListByExample.iterator();
        while (iterator.hasNext()) {
            HfmdEntity hfmdEntity = iterator.next();
            if(entity.getText().equals(hfmdEntity.getHfmdEntityCode())) {
                iterator.remove();
                return hfmdEntity;
            }
        }
        return null;
    }

}