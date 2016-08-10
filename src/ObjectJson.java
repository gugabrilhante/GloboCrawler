import java.lang.reflect.Field;
import java.util.Date;


public class ObjectJson {

	
	@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        Class<?> thisClass = null;
        try {
            thisClass = Class.forName(this.getClass().getName());// pega o nome da classe

            Field[] aClassFields = thisClass.getDeclaredFields(); // pega as variaveis da classe
            sb.append("{ "); //abre o json
            int i=0;
            for(Field f : aClassFields){
                //filtra: apenas String,int,Integer e float
                if(f.getType().isAssignableFrom(String.class) || f.getType().isAssignableFrom(Integer.class)
                        || f.getType().isAssignableFrom(Float.class) || f.getType().isAssignableFrom(int.class)
                        || f.getType().isAssignableFrom(Double.class)|| f.getType().isAssignableFrom(double.class) || f.getType().isAssignableFrom(Date.class)){
                    if(i>0)sb.append(",");
                    String fName = f.getName();
                    sb.append( "\"" + fName + "\"" + ":" + "\"" + f.get(this) + "\"");  //coloca da forma "nomeCampo":"valorCampo"
                    i++;
                }/*else if(f.getType().isAssignableFrom(ObjectJson.class)){

                }*/

            }
            sb.append("}"); // fecha o json
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sb.toString();
 }
	
}
