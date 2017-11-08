package com.seuic.android;

interface ICCardGetEccListener {
	void OnIcCardInsert();
	void OnSuccess( in byte[] eccBalance );
	void OnFail( int returnCode );
}
