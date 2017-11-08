package com.seuic.android;

interface ICCardTransFinishListener {
	void OnTC( in byte[] outdata, int outdata_len, in byte[] script_result, int script_result_len );
	void OnAAC( in byte[] outdata, int outdata_len, in byte[] script_result, int script_result_len );
	void OnFail( int returnCode );
}
