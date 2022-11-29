package effectivejava.item22;

import java.io.ObjectInputStream;
import java.io.ObjectStreamConstants;

public class Calculations implements Constants {

    public static double getReducedPlanckConstant() {
        return PLANCK_CONSTANT / (2 * PI);
    }

    public static void main(String[] args) {

        Integer a;
        ObjectStreamConstants b; //constant interface
        ObjectInputStream c; //class

        System.out.println(getReducedPlanckConstant());
    }
}

