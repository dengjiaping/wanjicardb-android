package com.seuic.android;
import com.seuic.android.ICCardGetEccListener;
import com.seuic.android.ICCardTransStartListener;
import com.seuic.android.ICCardTransFinishListener;

interface ICCardReader {
	int getEccBalance( ICCardGetEccListener listener, int overtime );
	int transStart( ICCardTransStartListener listener, in byte[] amount, in byte[] date, in byte[] time, byte transtype, byte key_index, byte track_type, in byte[] track_key, int overtime );
	int transFinish( ICCardTransFinishListener listener, in byte[] issuer_data, int issuer_data_len, int overtime );
	int stopRequest();
	int stopWaitTransFinish();
	int downloadAID( in byte[] aid );
	int clearAIDs();
	int downloadPubkey( in byte[] pubkey );
	int clearPubkeys();
	int setQPbocTransType( byte type );
}
