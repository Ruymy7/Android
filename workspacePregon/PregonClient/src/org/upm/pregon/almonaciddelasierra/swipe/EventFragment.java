 /*******************************************************************************
 * Copyright (C) 2014 Open University of The Netherlands
 * Author: Bernardo Tabuenca Archilla
 * LearnTracker project 
 * 
 * This library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package org.upm.pregon.almonaciddelasierra.swipe;

 import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.upm.pregon.almonaciddelasierra.R;



public class EventFragment extends Fragment {
	
	private String CLASSNAME = this.getClass().getName();

	public static final String ARG_POSITION = "position";
	
	public static final String ARG_TITLE = "title";
	public static final String ARG_SUBTITLE = "subtitle";
	public static final String ARG_SUBSUBTITLE = "subsubtitle";
	

	private View rootView;
	private int mPosition = 0;
	private String mTitle 		= "";
	private String mSubTitle 	= "";
	private String mSubSubTitle = "";
	
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		rootView = inflater.inflate(R.layout.fragment_collection_object, container, false);
		
		Bundle args = getArguments();
		mPosition = args.getInt(ARG_POSITION);
		mTitle = (String)args.getString(ARG_TITLE);
		mSubTitle = (String)args.getString(ARG_SUBTITLE);
		mSubSubTitle = (String)args.getString(ARG_SUBSUBTITLE);
		
		
		inflateLayout();
				
		Log.d(CLASSNAME, "onCreateView EventFragment item position["+mPosition+"] title["+mTitle+"]  TopText["+ mSubTitle +"]  LongText["+ mSubSubTitle +"].");
		
		return rootView;
		
	}

	public void inflateLayout(){
		
		TextView tTop = (TextView) rootView.findViewById(R.id.tvTitle);
		tTop.setText(mSubTitle);

		TextView tMid = (TextView) rootView.findViewById(R.id.tvSubTitle);
		tMid.setText(mSubTitle);

		TextView tBottom = (TextView) rootView.findViewById(R.id.tvSubSubTitle);

		// Temporary commented
		// tMid.setText(mSubSubTitle);

		tBottom.setText("En  un lugar de la Mancha, de cuyo nombre no quiero acordarme, no ha mucho tiempo que vivía un hidalgo de los de lanza en astillero, adarga antigua, rocín flaco y galgo corredor. Una olla de algo más vaca que carnero, salpicón las más noches, duelos y quebrantos los sábados, lentejas los viernes, algún palomino de añadidura los domingos, consumían las tres partes de su hacienda. El resto della concluían sayo de velarte, calzas de velludo para las fiestas con sus pantuflos de lo mismo, los días de entre semana se honraba con su vellori de lo más fino. Tenía en su casa una ama que pasaba de los cuarenta, y una sobrina que no llegaba a los veinte, y un mozo de campo y plaza, que así ensillaba el rocín como tomaba la podadera. Frisaba la edad de nuestro hidalgo con los cincuenta años, era de complexión recia, seco de carnes, enjuto de rostro; gran madrugador y amigo de la caza. Quieren decir que tenía el sobrenombre de Quijada o Quesada (que en esto hay alguna diferencia en los autores que deste caso escriben), aunque por conjeturas verosímiles se deja entender que se llama Quijana; pero esto importa poco a nuestro cuento; basta que en la narración dél no se salga un punto de la verdad.");

	}
	
	
	public int getPosition(){
		return mPosition;
	}
	
	

	
}