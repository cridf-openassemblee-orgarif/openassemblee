package openassemblee.service.util;

public class DichotomyUtil {

    static boolean debug = false;

    public interface Calculator {
        double calculate(double value);
    }

    // algo super limité en fait, marche bien si on a le bon intervalle dès le début notamment
    public static double dichotomiseMe(
        double value,
        double nextDiff,
        // on visera moins si la réponse est positive (et pas assez précise), plus si la réponse
        // est négative => prevoir la fonction pour ça !
        Calculator calculator,
        double expectedPrecision,
        int iteration
    ) {
        if (iteration > 100) {
            throw new IllegalStateException("Max iterations reached...");
        }

        double result = calculator.calculate(value);
        if (Math.abs(result) <= expectedPrecision) {
            if (debug) {
                System.out.println(iteration + " iterations");
            }
            return value;
        }
        if (result > 0) {
            // pas si simple ? des Math.abs au minimum ? ce que ça signifie comme contrainte pour écriture du calculator
            //            if (nextDiff >= value) {
            //                throw new IllegalArgumentException();
            //            }
            // TODO pas bon, il faudrait faire - nextDiff / 2, + nextDiff dans l'autre sens SANS diviser par deux
            // ou alors il y a bien deux période... d'abord être sûr qu'on enferme, ensuite dichotomy
            return dichotomiseMe(
                value - nextDiff,
                nextDiff / 2,
                calculator,
                expectedPrecision,
                iteration + 1
            );
        } else {
            return dichotomiseMe(
                value + nextDiff,
                nextDiff / 2,
                calculator,
                expectedPrecision,
                iteration + 1
            );
        }
    }
}
