package utils;

import javax.servlet.http.HttpServletRequest;

import static constants.Constants.PARAMETER_ERROR_INT;

/**
 * Created by amirshavit on 10/13/17.
 */
public class RequestUtils {

    public static int getIntParameter(HttpServletRequest request, String name) {
        String value = request.getParameter(name);
        if (value != null) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException numberFormatException) { }
        }
        return PARAMETER_ERROR_INT;
    }

}
