package mierzwa.rafal.smartmouse2;


import java.util.Set;

import android.annotation.TargetApi;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
* Po wybraniu urz�dzenia,When a device is chosen
 * jego adres MAC jest odsy�any do nadrz�dnej Aktywno�ci 
 */
@TargetApi(11)
public class BluetoothDeviceListActivity extends Activity {
    // Debugowanie
    private static final String TAG = "DeviceListActivity";
    private static final boolean D = true;

    public static String EXTRA_DEVICE_ADDRESS = "device_address";

    // Pola klasy
    private BluetoothAdapter mBtAdapter;
    private ArrayAdapter<String> mPairedDevicesArrayAdapter;
    private ArrayAdapter<String> mNewDevicesArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Nowe okno
//        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.device_list);

        // W przypadku anulowania ustaw wynik CANCELED
        setResult(Activity.RESULT_CANCELED);

        // Utw�rz przycisk do przeszukiwania otoczenia
        Button scanButton = (Button) findViewById(R.id.button_scan);
        scanButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                doDiscovery();
                v.setVisibility(View.GONE);
            }
        });

        // Inicjalizacja tablic. Jednej dla aktualnie sparowanych urz�dze� i drugiej dla nowo odkrytych
        mPairedDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.device_name);
        mNewDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.device_name);

        // Konfiguracja widoku listy dla sparowanych urz�dze�
        ListView pairedListView = (ListView) findViewById(R.id.paired_devices);
        pairedListView.setAdapter(mPairedDevicesArrayAdapter);
        pairedListView.setOnItemClickListener(mDeviceClickListener);

        // Konfiguracja widoku listy dla nowo wykrytych urz�dze�
        ListView newDevicesListView = (ListView) findViewById(R.id.new_devices);
        newDevicesListView.setAdapter(mNewDevicesArrayAdapter);
        newDevicesListView.setOnItemClickListener(mDeviceClickListener);

        // Zarejestruj nadawanie po wykryciu urz�dzenia
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, filter);

        // Zarejestruj nadawnie po zako�czeniu wykrywania
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mReceiver, filter);

        // Pobierz lokalny adapter Bluetooth
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();

        // Pobierz zestaw aktualnie sparowanych urz�dze�
        Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();

        // W przypadku istnienia urz�dze� sparowanych dodaj je do obiektu ArrayAdapter
        if (pairedDevices.size() > 0) {
            findViewById(R.id.title_paired_devices).setVisibility(View.VISIBLE);
            for (BluetoothDevice device : pairedDevices) {
                mPairedDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
            }
        } else {
            String noDevices ="none_paired";
            mPairedDevicesArrayAdapter.add(noDevices);
        }
    }

    @Override
    protected void onDestroy() {;
        super.onDestroy();

        // Upewnij si� �e nie jest wykonywane ponowne wykrywanie
        if (mBtAdapter != null) {
            mBtAdapter.cancelDiscovery();
        }

        // Usu� rejestracj� metod nas�uchuj�cych
        this.unregisterReceiver(mReceiver);
    }

    /**
     * Uruchom wykrywaie urz�dze� za pomoc� obiektu BluetoothAdapter
     */
    private void doDiscovery() {
        if (D) Log.d(TAG, "doDiscovery()");

        //  Sygnalizuj wyszukiwanie w nag��wku
        setProgressBarIndeterminateVisibility(true);
        setTitle("scanning");

        // Dla nowych urz�dze� wy�wietl list�
        findViewById(R.id.title_new_devices).setVisibility(View.VISIBLE);

        // Zako�cz ewentualne r�wnoleg�� wykrywanie
        if (mBtAdapter.isDiscovering()) {
            mBtAdapter.cancelDiscovery();
        }

        // Zarz�daj wyszukiwania od obiektu klasy BluetoothAdapter
        mBtAdapter.startDiscovery();
    }

    // Metoda nas�uchuj�ca klikni�cia na li�cie urz�dze� 
    private OnItemClickListener mDeviceClickListener = new OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
            // Zako�cz wyszukiwanie poniewa� jest ono kosztowne a zaraz zostanie nawi�zanie po��czenie
            mBtAdapter.cancelDiscovery();

            // Pobierz adres MAC
            String info = ((TextView) v).getText().toString();
            String address = info.substring(info.length() - 17);

            Intent intent = new Intent();
            intent.putExtra(EXTRA_DEVICE_ADDRESS, address);

            // Wy�lij wynik i zako�cz aktywno��
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    };

    //  BroadcastReceiver nas�uchuj�cy wykrytych urz�dze� i zmieniaj�cy nag��wek po wykryciu urz�dzenia

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            // Kiedy wyszukiwanie odkryje urz�dzenie
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Pobierz obiekt BluetoothDevice 
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // Je�eli jest ju� sparowane pomi�
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    mNewDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                }
            // Po sko�czeniu wyszukiwania, zmie� nag��wek Aktywno�ci
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                setProgressBarIndeterminateVisibility(false);
                setTitle("select_device");
                if (mNewDevicesArrayAdapter.getCount() == 0) {
                    String noDevices ="none_found";
                    mNewDevicesArrayAdapter.add(noDevices);
                }
            }
        }
    };

}
