package com.seuic.android;

interface TerminalInfo {
	int SetSKey( in byte[] skey );
	int GetSKey( out byte[] skey, out int[] skey_len );
	int SetCustomNum( in byte[] num );
	int GetCustomNum( out byte[] num, out int[] num_len );
	int SetCustomName( in byte[] name );
	int GetCustomName( out byte[] name, out int[] name_len );
	int SetTermNum( in byte[] num );
	int GetTermNum( out byte[] num, out int[] num_len );
}
