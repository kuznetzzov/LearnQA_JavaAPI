import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Ex10Test {

    @Test
    public void checkString(){
        String txt = "Теория без практики мертва, практика без теории слепа";
        Assertions.assertTrue(checkLength(txt), "Во фразе: " + txt + " - меньше 15 символов");
    }

    Boolean checkLength(String txt){
        boolean trueTxt = false;
        if (txt.length() > 15){
            trueTxt = true;
        }
        return trueTxt;
    }
}
