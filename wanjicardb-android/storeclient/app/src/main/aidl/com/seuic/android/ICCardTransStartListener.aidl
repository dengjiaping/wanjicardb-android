package com.seuic.android;

interface ICCardTransStartListener {
	void OnIcCardInsert();
	void OnUseICCard();
	void OnFallback();
	void OnICCardField55( in byte[] pan, int pan_len, in byte[] feild55, int field55_len, in byte[] track, int trackLen, byte panSeq );
	void OnTrackPlain( in byte[] pan, int pan_len, in byte[] trackData, int trackLen, in byte[] track2Data, int track2Len, in byte[] track3Data, int track3Len );
	void OnFail( int returnCode );
	void OnQPbocField55( boolean is_tc, in byte[] pan, int pan_len, in byte[] feild55, int field55_len, in byte[] track, int trackLen, byte panSeq );
}
