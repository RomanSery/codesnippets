package org.coderdreams.util;


import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;

public final class FieldArgs {
    private final String id;
    private final String fieldLabel;
    private final String propertiesId;
    private final IModel model;
    private final IModel choiceList;
    private final IModel<Boolean> isEnabledModel;
    private final IModel<String> txtModel;
    private final IChoiceRenderer cr;
    private final Class inputType;
    private final String markupId;

    private FieldArgs(Builder builder) {
        this.id = builder.id;
        this.fieldLabel = builder.fieldLabel;
        this.propertiesId = builder.propertiesId;
        this.model = builder.model;
        this.isEnabledModel = builder.isEnabledModel;
        this.cr = builder.cr;
        this.inputType = builder.inputType;
        this.choiceList = builder.choiceList;
        this.txtModel = builder.txtModel;
        this.markupId = builder.markupId;
    }

    public String getId() { return id; }
    public String getFieldLabel() { return fieldLabel; }
    public String getPropertiesId() { return propertiesId; }
    public IModel getModel() { return model; }
    public IModel<Boolean> getIsEnabledModel() { return isEnabledModel; }
    public IChoiceRenderer getCr() { return cr; }
    public Class getInputType() { return inputType; }
    public IModel getChoiceList() { return choiceList; }
    public IModel<String> getTxtModel() { return txtModel; }
    public String getMarkupId() { return markupId; }

    public static final class Builder {
        private final String id;
        private final String fieldLabel;
        private String propertiesId;
        private IModel model;
        private IModel choiceList;
        private IModel<Boolean> isEnabledModel;
        private IModel<String> txtModel;
        private IChoiceRenderer cr;
        private Class inputType;
        private String markupId;

        private Builder(String id, String fieldLabel, IModel model) {
            this.id = id;
            this.fieldLabel = fieldLabel;
            this.model = model;
        }

        public static Builder of(String id, String fieldLabel, IModel model) {
            return new Builder(id, fieldLabel, model);
        }

        public Builder model(IModel model) {
            this.model = model;
            return this;
        }
        public Builder isEnabledModel(IModel<Boolean> isEnabledModel) {
            this.isEnabledModel = isEnabledModel;
            return this;
        }
        public Builder propertiesId(String propertiesId) {
            this.propertiesId = propertiesId;
            return this;
        }
        public Builder cr(IChoiceRenderer cr) {
            this.cr = cr;
            return this;
        }
        public Builder inputType(Class inputType) {
            this.inputType = inputType;
            return this;
        }
        public Builder choiceList(IModel choiceList) {
            this.choiceList = choiceList;
            return this;
        }
        public Builder txtModel(IModel<String> txtModel) {
            this.txtModel = txtModel;
            return this;
        }
        public Builder markupId(String markupId) {
            this.markupId = markupId;
            return this;
        }

        public FieldArgs build() {
            return new FieldArgs(this);
        }
    }
}
