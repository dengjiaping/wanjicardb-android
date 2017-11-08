package com.seuic.android;


interface PinpadEnterPinListener {
	void OnSuccess( in byte[] enc, int enc_len );
	void OnFail( int error_code );
	void OnInput( int input_num );
}

