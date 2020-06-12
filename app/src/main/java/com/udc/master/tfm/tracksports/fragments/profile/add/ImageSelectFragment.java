package com.udc.master.tfm.tracksports.fragments.profile.add;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import com.udc.master.tfm.tracksports.R;
import com.udc.master.tfm.tracksports.bbdd.profiles.Profile;
import com.udc.master.tfm.tracksports.common.dialog.CustomDialogBuilder;
import com.udc.master.tfm.tracksports.common.dialog.DialogArrayAdapter;
import com.udc.master.tfm.tracksports.common.dialog.DialogItem;
import com.udc.master.tfm.tracksports.utils.ConstantsUtils;
import com.udc.master.tfm.tracksports.utils.FileUtils;

/**
 * Fragmento para recoger la imagen de perfil del usuario
 * @author a.oteroc
 *
 */
public class ImageSelectFragment extends AddProfileAbstractFragment {

	/** Carpeta donde se almacenan las imagenes tomadas con la camara */
	private static final String PICK_CAMERA_FOLDER_NAME = "TrackSports";
	/** Nombre de las imagenes tomadas con la camara */
	private static final String PICK_CAMERA_FILE_NAME = "profile_TrackSports_";
	
    /** Imagen con la foto de perfil del usuario */
	private ImageView profileImage;
	/** Boton para seleccionar la imagen */
	private Button imagePickerButton;
	
	private Uri imageUri;
	private String imagePath;
	
	/**
	 * Constructor de la clase
	 */
	public ImageSelectFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final Resources resources = getActivity().getResources();
		View imageSelectView = inflater.inflate(R.layout.fragment_image_select, container, false);
		
		setPreviousButton((ImageButton) imageSelectView.findViewById(R.id.button_image_select_previous));
		setNextButton((ImageButton) imageSelectView.findViewById(R.id.button_image_select_next));
		profileImage = (ImageView) imageSelectView.findViewById(R.id.imageView_picker);
		imagePickerButton = (Button) imageSelectView.findViewById(R.id.button_image_picker);
		
		fillParams();
		
		//Se crea el dialogo para seleccionar el metodo de obtener la imagen de perfil
    	DialogArrayAdapter dialogAdapter = new DialogArrayAdapter(getActivity(), getItems(resources));
    	
		CustomDialogBuilder builder = new CustomDialogBuilder(getActivity());
		builder.setTitle(resources.getString(R.string.image_picker_title_message));
		builder.setAdapter(dialogAdapter, new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		    	//Si se selecciona la primera opcion se obtiene la imagen de un fichero
		    	//Sino se obtiene de la camara
		    	if (which == ImagePickerType.PICK_FROM_FILE.getId()) {
	                Intent intent = new Intent();
	                intent.setType("image/*");
	                intent.setAction(Intent.ACTION_GET_CONTENT);
	                startActivityForResult(Intent.createChooser(intent, 
	                		resources.getString(R.string.image_picker_finish_action)), ImagePickerType.PICK_FROM_FILE.getId());
		    	} else {
	                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	                //Se crea la carpeta si no existe
	                File folder = new File(Environment.getExternalStorageDirectory()+"/"+PICK_CAMERA_FOLDER_NAME);
	                boolean success = false;
	                if (!folder.exists()) {
	                	success = folder.mkdir();
	                } else {
	                	success = true;
	                }
	                //Se guarda la imagen
	                if (success) {
		                File file = new File(Environment.getExternalStorageDirectory()+"/"+PICK_CAMERA_FOLDER_NAME,
		                		PICK_CAMERA_FILE_NAME + String.valueOf(System.currentTimeMillis()) + ".jpg");
		                try {
		                	imageUri = Uri.fromFile(file);
		                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		                    intent.putExtra("return-data", true);
		                    startActivityForResult(intent, ImagePickerType.PICK_FROM_CAMERA.getId());
		                } catch (Exception e) {
		                    e.printStackTrace();
		                }	
	                }
		    	}
		    	
		    }
		});
    	final AlertDialog dialog = builder.create();

    // Se muestra el dialogo para elegir imagen
    imagePickerButton.setOnClickListener(
        new OnClickListener() {
          @Override
          public void onClick(View arg0) {
            dialog.show();
            fillParams();
          }
        });
		fillParams();
		//Se navega al fragmento de detalles de usuario
		getPreviousButton().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				setParams();
				getActivity().getSupportFragmentManager().beginTransaction()
				.replace(R.id.container, new UserDetailsFragment()).commit();
			}
		});
		fillParams();
		//Se navega al fragmento de seleccionar las coordenadas de la casa
		getNextButton().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				setParams();
				getActivity().getSupportFragmentManager().beginTransaction()
				.replace(R.id.container, new MapPositionFragment()).commit();
			}
		});
		fillParams();
		return imageSelectView;
	}
	
	protected void fillParams() {
		Profile profile = (Profile)getActivity().getIntent().getSerializableExtra(ConstantsUtils.PROFILE_PARAM);
		if (profile != null) {
			try {
				imagePath = profile.getImagePath();
				if (imagePath != null) {
					Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
					profileImage.setImageBitmap(bitmap);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	protected void setParams() {
		Profile profile = (Profile)getActivity().getIntent().getSerializableExtra(ConstantsUtils.PROFILE_PARAM);
		profile.setImagePath(imagePath);
		
		getActivity().getIntent().putExtra(ConstantsUtils.PROFILE_PARAM, profile);
	}
	
	@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
		 //Si se ha producido algun error al obtener la imagen se detiene el procesamiento
		if (resultCode != Activity.RESULT_OK)
			return;
        
		Bitmap bitmap = null;
		
        //La imagen se obtiene de un fichero
        if (ImagePickerType.PICK_FROM_FILE.getId().equals(requestCode)) {
        	imageUri = data.getData();
            imagePath = FileUtils.getPath(getActivity(), imageUri);
            if (imagePath != null)
                bitmap = BitmapFactory.decodeFile(imagePath);
            
        //La imagen se obtiene de la camara
        } else {
        	imagePath = imageUri.getPath();
            bitmap = BitmapFactory.decodeFile(imagePath);
        }
 
        //Se setea la imagen con la seleccionada
        profileImage.setImageBitmap(bitmap);
	}
    
    /**
     * Metodo que obtiene la lista de elementos para mostrar
     * en el dialogo de seleccionar imagen
     * @param resources
     * @return
     */
    private List<DialogItem> getItems(Resources resources) {
    	List<DialogItem> items = new ArrayList<DialogItem>();
    	
    	String [] elements = resources.getStringArray(R.array.imagePickerDialog);
    	for (int i = 0; i < elements.length; i++) {
    		Drawable image = null;
    		if (i == ImagePickerType.PICK_FROM_CAMERA.getId()) {
    			image = resources.getDrawable(R.drawable.camera);
    		} else {
    			image = resources.getDrawable(R.drawable.search_file);
    		}
    		DialogItem item = new DialogItem(image, elements[i]);
    		items.add(item);
		}
    	return items;
    }
}
