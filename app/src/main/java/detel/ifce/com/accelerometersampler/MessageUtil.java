package detel.ifce.com.accelerometersampler;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.widget.Toast;


/**
 * Classe responsável por simplificar a exibição de mensagens através de Toasts
 */
public class MessageUtil {
	
	private static final String noNetworkConnectivity = "Sem conexão de Internet";


    /**
     * Mensagem de falta de conexão com a internet
     * @param context A activity onde será exibida a mensagem
     */
	public static void noNetworkConnectivity(Context context) {
		Toast message = getToast(context, noNetworkConnectivity);
		message.show();
	}

    /**
     * Constrói uma mensagem personalizada
     * @param context Acivity onde a mensagem será mostrada
     * @param msg A própria mensagem
     */
	public static void showMessage(Context context, String msg) {
		Toast message = getToast(context, msg);
		message.show();
	}

    /**
     * Constrói um Toast com a mensagem passada e retorna esse Toas para uso futuro
     * @param context A activity onde será exibido o Toast
     * @param msg A mensagem em si
     * @return Retorna o Toast construído
     */
	private static Toast getToast(Context context, String msg) {
		Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		return toast;
	}




}
