package com.seuic.android;

import com.seuic.android.PinpadEnterPinListener;

interface Pinpad {
	int GetSN( out byte[] sn, out int[] sn_len );
	int DownloadPinK( byte mk_index, byte index, in byte[] key );
	int DownloadMacK( byte mk_index, byte index, in byte[] key );
	int DownloadDesK( byte mk_index, byte index, in byte[] key );
	int EnterPin( PinpadEnterPinListener listener, byte pink_index, byte is_desk_enc_need, byte desk_index, in byte[] track_key, int overtime );
	int StopEnterPin();
	int Mac( byte key_index, in byte[] inData, out byte[] mac );
	int SetPinLen( in byte[] pinLen );
	int SetMacType( byte type );
}
