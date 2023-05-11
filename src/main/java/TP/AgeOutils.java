package TP;

public class AgeOutils {

    public static void CheckAge(int age) throws TooyoungException,ToooldException {
        if (age < 18){
            throw new TooyoungException("Age "+age+" très jeune");
        }else if (age > 40) {
            throw new ToooldException("Age "+age+" Trop Agée");
        }
        System.out.println(age + age + "OK");
    }
}
