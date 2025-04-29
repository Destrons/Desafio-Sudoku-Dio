package Model;

public enum GameStatusEnum {

    NON_STARTED("não iniciado"),
    INCOMPLETE("incompleto"),
    COMPLETE("completo");
    
    private String label;

    private GameStatusEnum(final String label){
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}