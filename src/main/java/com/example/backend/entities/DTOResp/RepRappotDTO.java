package com.example.backend.entities.DTOResp;

public class RepRappotDTO {
    private String chart_type;
    private String name;
    private String seriesSubtitle;
    private String title;
    private String fieldrepport_merge;
    private String operetionfieldmerge;
    private boolean isFieldMerge;
    private String col1;
    private String col2;
    private String table_join;
    private boolean isJoinTable;
    private boolean isYcustField;
    private boolean isCustomize;
    private boolean isDetails;
    private String type_flow;
    private Long functionId;
    private boolean isPercent;
    private String groupByField;
    private Integer limitNumber;
    private boolean percent;
    private boolean isGroupedBy;
    private boolean isLimited;
    private boolean isSimplePie;
    private boolean isOperator;
    private boolean load;
    private boolean isCarrier;
    private boolean isNested;
    private boolean isCharacter;
    private boolean isDiff;
    private boolean hasDetails;
    private boolean hasDate;



    public RepRappotDTO() {
    }



    public String getChart_type() {
    return chart_type;
  }

  public void setChart_type(String chart_type) {
    this.chart_type = chart_type;
  }

  public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSeriesSubtitle() {
        return seriesSubtitle;
    }

    public void setSeriesSubtitle(String seriesSubtitle) {
        this.seriesSubtitle = seriesSubtitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isFieldMerge() {
        return isFieldMerge;
    }

    public void setFieldMerge(boolean fieldMerge) {
        isFieldMerge = fieldMerge;
    }

    public String getCol1() {
        return col1;
    }

    public void setCol1(String col1) {
        this.col1 = col1;
    }

    public String getCol2() {
        return col2;
    }

    public void setCol2(String col2) {
        this.col2 = col2;
    }
    public boolean isJoinTable() {
        return isJoinTable;
    }

    public void setJoinTable(boolean joinTable) {
        isJoinTable = joinTable;
    }

    public boolean isYcustField() {
        return isYcustField;
    }

    public void setYcustField(boolean ycustField) {
        isYcustField = ycustField;
    }

    public boolean isCustomize() {
        return isCustomize;
    }

    public void setCustomize(boolean customize) {
        isCustomize = customize;
    }

    public boolean isDetails() {
        return isDetails;
    }

    public void setDetails(boolean details) {
        isDetails = details;
    }

    public Long getFunctionId() {
        return functionId;
    }

    public void setFunctionId(Long functionId) {
        this.functionId = functionId;
    }

    public boolean isPercent() {
        return isPercent;
    }

    public void setPercent(boolean percent) {
        isPercent = percent;
    }

    public boolean isGroupedBy() {
        return isGroupedBy;
    }

    public void setGroupedBy(boolean groupedBy) {
        isGroupedBy = groupedBy;
    }

    public boolean isLimited() {
        return isLimited;
    }

    public void setLimited(boolean limited) {
        isLimited = limited;
    }

    public boolean isSimplePie() {
        return isSimplePie;
    }

    public void setSimplePie(boolean simplePie) {
        isSimplePie = simplePie;
    }

    public boolean isOperator() {
        return isOperator;
    }

    public void setOperator(boolean operator) {
        isOperator = operator;
    }

    public boolean isLoad() {
        return load;
    }

    public void setLoad(boolean load) {
        this.load = load;
    }

    public boolean isCarrier() {
        return isCarrier;
    }

    public void setCarrier(boolean carrier) {
        isCarrier = carrier;
    }

    public boolean isNested() {
        return isNested;
    }

    public void setNested(boolean nested) {
        isNested = nested;
    }

    public boolean isCharacter() {
        return isCharacter;
    }

    public void setCharacter(boolean character) {
        isCharacter = character;
    }

    public boolean isDiff() {
        return isDiff;
    }

    public void setDiff(boolean diff) {
        isDiff = diff;
    }

    public boolean isHasDetails() {
        return hasDetails;
    }

    public void setHasDetails(boolean hasDetails) {
        this.hasDetails = hasDetails;
    }

    public boolean isHasDate() {
        return hasDate;
    }

    public void setHasDate(boolean hasDate) {
        this.hasDate = hasDate;
    }

    public String getGroupByField() {
        return groupByField;
    }

    public void setGroupByField(String groupByField) {
        this.groupByField = groupByField;
    }

    public Integer getLimitNumber() {
        return limitNumber;
    }

    public void setLimitNumber(Integer limitNumber) {
        this.limitNumber = limitNumber;
    }

    public String getFieldrepport_merge() {
        return fieldrepport_merge;
    }

    public void setFieldrepport_merge(String fieldrepport_merge) {
        this.fieldrepport_merge = fieldrepport_merge;
    }

    public String getOperetionfieldmerge() {
        return operetionfieldmerge;
    }

    public void setOperetionfieldmerge(String operetionfieldmerge) {
        this.operetionfieldmerge = operetionfieldmerge;
    }

    public String getTable_join() {
        return table_join;
    }

    public void setTable_join(String table_join) {
        this.table_join = table_join;
    }

    public String getType_flow() {
        return type_flow;
    }

    public void setType_flow(String type_flow) {
        this.type_flow = type_flow;
    }
}
