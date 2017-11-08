package com.seuic.android;

interface MagCardSwipeListener {
	void OnSuccess( in byte[] pan, int panLen, in byte[] trackData, int trackLen, in byte[] track2Data, int track2Len, in byte[] track3Data, int track3Len );
	void OnFail( int returnCode );
}
