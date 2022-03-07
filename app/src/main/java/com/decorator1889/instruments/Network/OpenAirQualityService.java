package com.decorator1889.instruments.Network;


import com.decorator1889.instruments.Models.InstrumentModel;
import com.decorator1889.instruments.Models.SurgeryInstrumentModel;
import com.decorator1889.instruments.Models.TestModel;
import java.util.List;
import io.reactivex.Observable;
import retrofit2.http.POST;

public interface OpenAirQualityService {
    @POST("get-instrument.php")
    Observable<List<InstrumentModel>> fetchData();
    @POST("get-test.php")
    Observable<List<TestModel>> fetchTest();
    @POST("get-instrument-surgery.php")
    Observable<List<SurgeryInstrumentModel>> fetchSurgeryData();
}

