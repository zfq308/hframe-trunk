package com.hframework.web.bean;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hframework.common.util.ReflectUtils;
import com.hframework.common.util.ResourceWrapper;
import com.hframework.common.util.StringUtils;
import com.hframework.common.util.UrlHelper;
import com.hframework.web.CreatorUtil;
import com.hframework.web.config.bean.DataSet;
import com.hframework.web.config.bean.DataSetHelper;
import com.hframework.web.config.bean.DataSetRuler;
import com.hframework.web.config.bean.datasethelper.Mapping;
import com.hframework.web.config.bean.datasetruler.Rule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangquanhong on 2016/5/26.
 */
public class DataSetDescriptor {

    private DataSet dataSet;

    private Map<String, String> relFieldKeyMap = new HashMap<String, String>();

    //<hfpm_program/hfpm_program_id,ProgramDescriptor.class>
    private Map<String, DataSetDescriptor> relDataSetMap = new HashMap<String, DataSetDescriptor>();


    private List<DataSetHelper> dataSetHelpers = new ArrayList<DataSetHelper>();

    private List<DataSetRuler> dataSetRulers = new ArrayList<DataSetRuler>();

    private JSONObject dataSetRulerJsonObject = new JSONObject();

    private IDataSet dateSetStruct;

    public void addRelDataSet(String fieldName, String key, DataSetDescriptor descriptor) {
        relDataSetMap.put(key,descriptor);
        relFieldKeyMap.put(fieldName,key);
    }

    public DataSetDescriptor(DataSet dataSet) {
        this.dataSet = dataSet;
    }

    public DataSet getDataSet() {
        return dataSet;
    }

    public void setDataSet(DataSet dataSet) {
        this.dataSet = dataSet;
    }

    public Map<String, DataSetDescriptor> getRelDataSetMap() {
        return relDataSetMap;
    }

    public Map<String, String> getRelFieldKeyMap() {
        return relFieldKeyMap;
    }

    public Map<String, Object> getRelFieldValueMap(Object object) {
        Map<String, Object> result = new HashMap<String, Object>();
        for (String fieldCode : relFieldKeyMap.keySet()) {
            String propertyName = CreatorUtil.getJavaVarName(fieldCode);
            Object propertyValue = ReflectUtils.getFieldValue(object, propertyName);
            result.put(relFieldKeyMap.get(fieldCode), propertyValue);
        }


        return result;
    }

    public void addDataSetHelper(DataSetHelper dataSetHelper) {
        dataSetHelpers.add(dataSetHelper);
    }
    public void addDataSetRuler(DataSetRuler dataSetRuler) {
        dataSetRulers.add(dataSetRuler);
    }

    public List<DataSetHelper> getDataSetHelpers() {
        return dataSetHelpers;
    }

    public static Map<String, String> getConditionMap(String helpDatascore) {
        Map<String, String> urlParameters = UrlHelper.getUrlParameters("?" + helpDatascore, true);
        return urlParameters;

    }

    public JSONObject getDynamicHelper() {
        JSONObject result = new JSONObject();
        List<DataSetHelper> dataSetHelpers = this.dataSetHelpers;
        if(dataSetHelpers != null) {
            for (DataSetHelper dataSetHelper : dataSetHelpers) {
                String helpDatascore = dataSetHelper.getHelpDatascore();
                if(StringUtils.isNotBlank(helpDatascore)) {
                    Map<String, String> condition = getConditionMap(helpDatascore);
                    for (String propertyName : condition.keySet()) {
                        String propertyValue = condition.get(propertyName);
                        if(!propertyValue.startsWith("{") && !propertyValue.endsWith("}") ) {
                            continue ;
                        }
                        propertyValue = propertyValue.substring(1, propertyValue.length() - 1);
                        String referDataSetCode = propertyValue.substring(0, propertyValue.indexOf("/"));
                        String referDataFiledCode = propertyValue.substring(propertyValue.indexOf("/") + 1);
                        String referDataFiledPropertyName = CreatorUtil.getJavaVarName(referDataFiledCode);

                        JSONObject object = new JSONObject();
                        object.put("sourceCode",referDataFiledPropertyName);
                        object.put("targetCode",propertyValue);
                        object.put("ruleType",3);
                        List<Mapping> mappingList = dataSetHelper.getMappings().getMappingList();
                        for (Mapping mapping : mappingList) {
                            if("true".equals(mapping.getIsCompareKey())) {
                                object.put("compareKey",CreatorUtil.getJavaVarName(mapping.getEffectDatasetField()));
                            }
                            if("true".equals(mapping.getIsCompareName())) {
                                object.put("compareName",CreatorUtil.getJavaVarName(mapping.getEffectDatasetField()));
                            }
                        }
                        result.put(referDataFiledPropertyName,object);
                    }
                }else {
                    JSONObject object = new JSONObject();
                    List<Mapping> mappingList = dataSetHelper.getMappings().getMappingList();
                    for (Mapping mapping : mappingList) {
                        if("true".equals(mapping.getIsCompareKey())) {
                            object.put("compareKey",CreatorUtil.getJavaVarName(mapping.getEffectDatasetField()));
                        }
                        if("true".equals(mapping.getIsCompareName())) {
                            object.put("compareName",CreatorUtil.getJavaVarName(mapping.getEffectDatasetField()));
                        }
                    }
                    result.put("NE",object);
                }
            }
        }
        return result;
    }



    public void setDataSetHelpers(List<DataSetHelper> dataSetHelpers) {
        this.dataSetHelpers = dataSetHelpers;
    }

    public List<DataSetRuler> getDataSetRulers() {
        return dataSetRulers;
    }

    public void setDataSetRulers() {
        for (DataSetRuler dataSetRuler : dataSetRulers) {
            List<Rule> ruleList = dataSetRuler.getRuleList();
            if(ruleList != null) {
                dataSetRulerJsonObject = new JSONObject();
                for (Rule rule : ruleList) {
                    String sourceCode = CreatorUtil.getJavaVarName(rule.getSourceCode());
                    String sourceValue = rule.getSourceValue();
                    String targetCode = CreatorUtil.getJavaVarName(rule.getTargetCode());
                    String targetValue = rule.getTargetValue();
                    String editable = rule.getEditable();
                    String key = sourceCode;
                    String ruleType = rule.getRuleType();
                    JSONObject object = new JSONObject();
                    object.put("sourceCode",sourceCode);
                    object.put("sourceValue",sourceValue);
                    object.put("targetCode",targetCode);
                    object.put("targetValue",targetValue);
                    object.put("editable",editable);
                    object.put("ruleType",ruleType);

                    if("1".equals(ruleType)) {//1值映射 2 值关联
                        key = key + "=" + sourceValue;
                    }
                    if(!dataSetRulerJsonObject.containsKey(key)) {
                        dataSetRulerJsonObject.put(key, new JSONArray());
                    }
                    JSONArray jsonArray = dataSetRulerJsonObject.getJSONArray(key);
                    jsonArray.add(object);
                }
            }
        }
    }

    public JSONObject getDataSetRulerJsonObject() {
        return dataSetRulerJsonObject;
    }

    public String[] getRelPropertyNames() {
        for (Map.Entry<String, String> entry : relFieldKeyMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if(value.startsWith(dataSet.getEventObjectCode() + "/")){
                return new String[]{
                        ResourceWrapper.JavaUtil.getJavaVarName(value.replace(dataSet.getEventObjectCode() + "/", "")),
                        ResourceWrapper.JavaUtil.getJavaVarName(key)};
            }
        }
        return null;
    }

    public IDataSet getDateSetStruct() {
        return dateSetStruct;
    }

    public void setDateSetStruct(IDataSet dateSetStruct) {
        this.dateSetStruct = dateSetStruct;
    }
}
