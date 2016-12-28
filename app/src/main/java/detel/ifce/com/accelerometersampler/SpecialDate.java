package detel.ifce.com.accelerometersampler;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Class para modelar funções especiais para as Datas usadas no app
 */
public class SpecialDate extends Date {

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    //Formaters para exibição de data
    private static SimpleDateFormat mPresenterDateFormatter = new SimpleDateFormat("dd/MM/yyyy");

    private static SimpleDateFormat mPresenterNewsDateFormatter = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
    //Formatter para utilizar a data em queries
    private static SimpleDateFormat mQueryDateFormatter = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

    //Construtor, seta a data de hoje
    public SpecialDate() {
        Calendar calendar = Calendar.getInstance();
        this.setTime(calendar.getTimeInMillis());
    }

    public SpecialDate(String date) {
        //ParsePosition pos = new ParsePosition(0);
        Date stringDate = null;
        try {
            stringDate = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.setTime(stringDate.getTime());
    }

    public SpecialDate(String date, SimpleDateFormat format) {
        //ParsePosition pos = new ParsePosition(0);
        Date stringDate = null;
        try {
            stringDate = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.setTime(stringDate.getTime());
    }

    public SpecialDate(Calendar calendar) {
        this.setTime(calendar.getTimeInMillis());
    }


    //Overide de funções depreciadas
    @Override
    public int getDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(calendar.getTimeInMillis());
        return calendar.get(Calendar.DAY_OF_MONTH);
    }


    @Override
    public int getMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(this.getTime());
        return calendar.get(Calendar.MONTH);
    }

    @Override
    public void setMonth(int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(this.getTime());
        calendar.set(Calendar.MONTH, month);
        this.setTime(calendar.getTimeInMillis());
    }

    @Override
    public int getYear() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(this.getTime());
        return calendar.get(Calendar.YEAR);
    }

    /**
     * Retorna o primeiro dia do mes daquela data
     *
     * @return Uma data em String com o primeiro dia daquele mês
     */
    public String getBeginDayForQuery() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(this.getTime());
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        return mQueryDateFormatter.format(calendar.getTime());
    }

    /**
     * Retorna o dia final do mês daquela data
     *
     * @return Uma data em string com o ultimo dia daquele mês
     */
    public String getFinalDayForQuery() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(this.getTime());
        int monthDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.DAY_OF_MONTH, monthDays);

        return mQueryDateFormatter.format(calendar.getTime());
    }

    /**
     * Formata uma data para query no banco/API
     *
     * @return A data formatada como yyyy-MM-dd
     */
    public String getDayForQuery() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(this.getTime());
        String time = mQueryDateFormatter.format(calendar.getTime());
        return time;
    }

    /**
     * Formata uma data para exibição
     *
     * @return A data formatada como dd-MM-yyyy
     */
    public String getDayForPresentation() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(this.getTime());
        String time = mPresenterDateFormatter.format(calendar.getTime());
        return time;
    }

    public String getDayForNewsPresentation() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(this.getTime());
        String time = mPresenterNewsDateFormatter.format(calendar.getTime());
        return time;
    }

    /**
     * Retorna o calendário desta data
     *
     * @return Um calendário desta data
     */
    public Calendar getCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(this.getTime());
        return calendar;
    }

    /**
     * Seta o tempo a partir dos parametros passados
     *
     * @param day   Um dia
     * @param month Um mês
     * @param year  Um ano
     */
    public void setTimeFromParam(int day, int month, int year) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, day, 0, 0);
        this.setTime(c.getTimeInMillis());
    }




    /**
     * Retorna a data do primeiro dia do mês atual
     *
     * @return Um objeto Special Date com a data do primeiro dia do mês
     */
    public SpecialDate getFirstDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(this.getTime());
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        return new SpecialDate(calendar);
    }

    /**
     * Retorna a data do ultimo dia do mês atual
     *
     * @return Um objeto Special Data com a data do último dia do mês
     */
    public SpecialDate getLastDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(this.getTime());
        int monthDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.DAY_OF_MONTH, monthDays);

        return new SpecialDate(calendar);
    }

    /**
     * Incrementa a data atual em 1 dia
     *
     * @return a data atual + 1 dia
     */
    public SpecialDate incrementDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(this.getTime() + 86400000);
        return new SpecialDate(calendar);
    }



    public boolean isBeforeSimple(SpecialDate otherDate) {
        if (this.getCalendar().get(Calendar.YEAR) < otherDate.getCalendar().get(Calendar.YEAR)) {
            return true;
        } else {

            if (this.getCalendar().get(Calendar.YEAR) > otherDate.getCalendar().get(Calendar.YEAR)) {
                return false;
            } else {
                if (this.getCalendar().get(Calendar.MONTH) < otherDate.getCalendar().get(Calendar.MONTH)) {
                    return true;
                } else {

                    if (this.getCalendar().get(Calendar.MONTH) > otherDate.getCalendar().get(Calendar.MONTH)) {
                        return false;
                    } else {
                        if (this.getCalendar().get(Calendar.DAY_OF_MONTH) < otherDate.getCalendar().get(Calendar.DAY_OF_MONTH)) {
                            return true;
                        } else {
                            return false;
                        }
                    }

                }
            }
        }

    }

    public boolean isEqualSimple(SpecialDate otherDate) {
        if ((otherDate.getCalendar().get(Calendar.YEAR) == this.getCalendar().get(Calendar.YEAR) && (otherDate.getCalendar().get(Calendar.MONTH) == this.getCalendar().get(Calendar.MONTH)) && (otherDate.getCalendar().get(Calendar.DAY_OF_MONTH) == this.getCalendar().get(Calendar.DAY_OF_MONTH)))) {
            return true;
        }

        return false;
    }

    public String getDateSinceFormatted() {
        Calendar calendar = Calendar.getInstance();

        long time = this.getTime();
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }

        long now = calendar.getTimeInMillis();
        if (time > now || time <= 0) {
            return null;
        }

        // TODO: localize
        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "agora";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "um minuto atrás";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " minutos atrás";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "uma hora atrás";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " horas atrás";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "ontem";
        } else {
            return diff / DAY_MILLIS + " dias atrás";
        }
    }

    public String getWholeDate() {
        String time = format.format(this.getTime());
        return time;
    }
}
