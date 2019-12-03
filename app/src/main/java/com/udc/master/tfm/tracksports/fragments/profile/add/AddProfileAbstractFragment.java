package com.udc.master.tfm.tracksports.fragments.profile.add;

import androidx.fragment.app.Fragment;
import android.widget.ImageButton;

/**
 * Clase que encapsula la funcionalidad comun de todos los fragmentos para anadir/editar un perfil
 * a la aplicacion
 * @author a.oteroc
 *
 */
public abstract class AddProfileAbstractFragment extends Fragment {

	/** Boton para ir al fragmento anterior */
	private ImageButton previousButton;
	/** Boton para ir al fragmento siguiente */
	private ImageButton nextButton;
	/** Boton para la creacion del perfil */
	private ImageButton finishButton;
	
	/**
	 * @return the previousButton
	 */
	public ImageButton getPreviousButton() {
		return previousButton;
	}

	/**
	 * @param previousButton the previousButton to set
	 */
	public void setPreviousButton(ImageButton previousButton) {
		this.previousButton = previousButton;
	}

	/**
	 * @return the nextButton
	 */
	public ImageButton getNextButton() {
		return nextButton;
	}

	/**
	 * @param nextButton the nextButton to set
	 */
	public void setNextButton(ImageButton nextButton) {
		this.nextButton = nextButton;
	}

	/**
	 * @return the finishButton
	 */
	public ImageButton getFinishButton() {
		return finishButton;
	}

	/**
	 * @param finishButton the finishButton to set
	 */
	public void setFinishButton(ImageButton finishButton) {
		this.finishButton = finishButton;
	}

	/**
	 * Clase que rellena los combos con la informacion del perfil pasada entre los fragmentos
	 */
	protected abstract void fillParams();
	
	/**
	 * Clase que setea los parametros del perfil introducidos en el fragmento actual
	 * para informar al resto de fragmentos y no perder los datos introducidos al navegar
	 */
	protected abstract void setParams();
}
