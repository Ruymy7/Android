package org.ounl.noisenotifier.fcube.config;



public class FeedbackCubeColorFactory {
	
	public static int COLOR_GRADIENT_SIZE = 512;
	private static int COLOR_GRADIENT_HALF_SIZE = 256;
	
	private FeedbackCubeColor[] mColorGradientArray = new FeedbackCubeColor[COLOR_GRADIENT_SIZE];
	

	public FeedbackCubeColorFactory(){
		
		for (int i = 0; i < (COLOR_GRADIENT_SIZE / 2); i++) {
			
			

						
			// From green to yellow
			mColorGradientArray[i] = new FeedbackCubeColor(i, 255, 0);
			
			// From yellow to red
			mColorGradientArray[i+COLOR_GRADIENT_HALF_SIZE] = new FeedbackCubeColor(255, 255-i, 0);

			
			
			
		}
		
		
	}
	
	/**
	 * Returns color for given index
	 * 
	 * @param index
	 * @return
	 */
	public FeedbackCubeColor getColor(int index){
		
		return mColorGradientArray[index];
	}
	

}
