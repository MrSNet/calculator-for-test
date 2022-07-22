import java.io.IOException;
import java.util.Scanner;

public class Main {
    static String expression;
    static String result;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите выражение:");
        System.out.println(calc(expression = scanner.nextLine()));
    }
    public static String calc(String input) {
        int[] resArabic = new int[2];
        byte number1, number2, number3 = 0; // 3 - результат
        String operation = null;
        String [] numbers = new String[0];
        boolean romanN = false;

        //input = expression.replaceAll("\\s",""); // первый вариант
        input = input.replaceAll("\\s","");
        char[] expressionArray = input.toCharArray();

        for (char c : expressionArray) {
            switch (c) {
                case 73, 86, 88 -> romanN = true;
            }
        }

        for (char c : expressionArray) { //получаю знак
            switch (c) {
                case 42 -> operation = String.valueOf(c); // *
                case 43 -> operation = String.valueOf(c); // +
                case 45 -> operation = String.valueOf(c); // -
                case 47 -> operation = String.valueOf(c); // /
            }
        }

        if (operation != null) {
            operation = operation.replace(operation,"\\"+operation); // заменяю знак
            numbers = input.split(operation);
        }

        if (operation == null) {
            try {
                throw new NullPointerException();
            } catch (NullPointerException e) {
                return "throws Exception //т.к. строка не является математической операцией";
            }
        }

        if (numbers.length != 2) {
                try {
                    throw new NullPointerException();
                } catch (NullPointerException e) {
                    return "throws Exception //т.к. формат математической операции" +
                            " не удовлетворяет заданию - два операнда и один оператор (+, -, /, *)";
                }
        }

       if (romanN) {
            try {
                for (int i = 0; i < 2; i++) {
                    resArabic [i] = RomanNumeral.valueOf(numbers[i].substring(numbers[i].length() - 1)).getArabic();
                }
            } catch (IllegalArgumentException e) {
                return "throws Exception //т.к. используются одновременно разные системы счисления";
            }
        }
        if (romanN & expressionArray[0] == '-'){
            try {
                throw new IOException();
            } catch (IOException e) {
                return "throws Exception //т.к. в римской системе нет отрицательных чисел";
            }
        }

        if (!romanN){
            number1 = Byte.parseByte(numbers[0]);
            number2 = Byte.parseByte(numbers[1]);
        }
        else {
            //преобразование римской цифры в арабскую
            for (int i = 0; i < 2; i++) {
                //берёт текущую римскую в формате арабской, если 1 символ. Если более, то берёт последнюю
                resArabic [i] = RomanNumeral.valueOf(numbers[i].substring(numbers[i].length() - 1)).getArabic();
                //если два символа в римской, то циклом преобразует в арабскую
                for (int idx = numbers[i].length() - 2; idx >= 0; idx--) {
                    RomanNumeral current = RomanNumeral.valueOf(numbers[i].substring(idx, idx + 1));
                    RomanNumeral next = RomanNumeral.valueOf(numbers[i].substring(idx + 1, idx + 2));
                    if (current.getArabic() < next.getArabic()) {
                        resArabic[i] -= current.getArabic();
                    } else {
                        resArabic[i] += current.getArabic();
                    }
                }
            }


            number1 = (byte)resArabic[0];
            number2 = (byte)resArabic[1];
        }
        if (number1 <= 0 || number2 <= 0 || number1 > 10 || number2 > 10){
            try {
                throw new IOException();
            } catch (IOException e) {
                return "throws Exception //т.к. числа" +
                        " не удовлетворяет заданию - от 1 до 10 включительно, не более и не менее";
            }
        }

        for (char c : expressionArray) { // вычисляю на основе полученного знака
            switch (c) {
                case 42 -> number3 = (byte) (number1 * number2); // *
                case 43 -> number3 = (byte) (number1 + number2); // +
                case 45 -> number3 = (byte) (number1 - number2); // -
                case 47 -> number3 = (byte) (number1 / number2); // /
            }
        }
        if (romanN && number3 <= 0){
            try {
                throw new IOException();
            } catch (IOException e) {
                return "throws Exception //т.к. в римской системе нет отрицательных чисел и ноля";
            }
        }

        if (!romanN){
            result = Byte.toString(number3);
        }
        else { //преобразование арабской цифры в римскую
            StringBuilder romanVal = new StringBuilder();
            // смотрит весь enum до конца, пока не кончится
            for(RomanNumeral rn :RomanNumeral.values()){//ищет максимальную и добавляет в строку
                // конвертирует пока результут (number3) >= римского из enum, которое в формате арабского
                while(number3 >= rn.getArabic()){ //перебирает цифры
                    romanVal.append(rn); //добавляет последовательно символ римских цифр
                    //вычетает из number3 найденное выше число, чтобы продолжить сравнивать результат
                    number3 -= rn.getArabic();
                }
            }
            result = romanVal.toString();
        }


        return "Результат: " + "\n" + result;
    }
}