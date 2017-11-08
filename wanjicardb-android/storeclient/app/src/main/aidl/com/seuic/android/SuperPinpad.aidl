package com.seuic.android;

import com.seuic.android.DownloadMKFromPCListener;

interface SuperPinpad {
	int DownloadMKFromPC( DownloadMKFromPCListener listener );
	int StopDonloadMKFromPC();
	int SetSN( in byte[] sn );
	int SetSNASCII( in byte[] sn );
	int DownloadPinMK( byte index, in byte[] key );
	int DownloadMacMK( byte index, in byte[] key );
	int DownloadDesMK( byte index, in byte[] key );
}
