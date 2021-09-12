package codeztalk.elbasha.delegate.view_models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import codeztalk.elbasha.delegate.helper.PreferenceHelper;
import codeztalk.elbasha.delegate.models.ConnectedDevice;
import codeztalk.elbasha.delegate.repositories.PrinterRepository;

public class PrinterViewModel extends ViewModel {
    private MutableLiveData<ConnectedDevice> mPrinter = new MutableLiveData<>();
    private final PrinterRepository mPrinterRepository = PrinterRepository.getInstance();

    public void loadPrinter(PreferenceHelper preferenceHelper){
        mPrinter = mPrinterRepository.getPrinter(preferenceHelper);
    }

    public LiveData<ConnectedDevice> getPrinter() {
        return mPrinter;
    }
}
