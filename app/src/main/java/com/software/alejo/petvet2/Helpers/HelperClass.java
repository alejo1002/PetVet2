package com.software.alejo.petvet2.Helpers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Clase con métodos estáticos como ayuda común de las demás clases.
 * Created by WEY on 27/11/2016.
 */
public class HelperClass {
    /**
     * Valida que un email tenga un formato correcto.
     * @param email el email a validar.
     * @return true: si el email es válido.
     *        false: si el email NO es válido.
     */
    public static boolean isValidEmail(String email) {
        boolean isValid = false;

        Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher matcher = pattern.matcher(email);
        if (matcher.find()) {
            isValid = true;
        }

        return isValid;
    }

}
