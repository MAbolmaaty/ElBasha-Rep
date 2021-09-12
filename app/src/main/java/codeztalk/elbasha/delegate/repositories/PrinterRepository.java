package codeztalk.elbasha.delegate.repositories;

import androidx.lifecycle.MutableLiveData;

import codeztalk.elbasha.delegate.helper.PreferenceHelper;
import codeztalk.elbasha.delegate.models.ConnectedDevice;

public class PrinterRepository {

    private static PrinterRepository instance;

    public static PrinterRepository getInstance() {
        if (instance == null){
            instance = new PrinterRepository();
        }
        return instance;
    }

    public MutableLiveData<ConnectedDevice> getPrinter(PreferenceHelper preferenceHelper){
        MutableLiveData<ConnectedDevice> printer = new MutableLiveData<>();
        printer.setValue(preferenceHelper.getPrinter());
        return printer;
    }
}
