package com.papaprogramador.presidenciales.Utils.StaticMethods;


import android.content.Context;

import com.papaprogramador.presidenciales.R;

public class GetString {

	public static String getStringShareCandidate(String nameCandidate, Context context) {


		String p1  = context.getResources().getString(R.string.share_candidate_part1);
		String p2  = context.getResources().getString(R.string.share_candidate_part2);

		String stringShareCandidate;
		stringShareCandidate = p1 + " " +  nameCandidate + " " + p2;

		return stringShareCandidate;
	}
}
