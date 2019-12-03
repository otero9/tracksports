package com.udc.master.tfm.tracksports.bbdd.profiles;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;

import com.udc.master.tfm.tracksports.bbdd.mapposition.MapPosition;
import com.udc.master.tfm.tracksports.directcom.rest.HttpStatusType;
import com.udc.master.tfm.tracksports.directcom.rest.object.AutenticationResponseWS;
import com.udc.master.tfm.tracksports.directcom.rest.object.EditRequestWS;
import com.udc.master.tfm.tracksports.directcom.rest.object.RegistrationRequestWS;
import com.udc.master.tfm.tracksports.utils.DateUtils;

/**
 * Clase que representa un perfil de la aplicacion
 * @author a.oteroc
 *
 */
public class Profile implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/** Identificador de BBDD del perfil */
	private Integer id;
	/** Identificador del servicio web */
	private String guid;
	/** Usuario del perfil para sincronizarse con la aplicacion Web*/
	private String user;
	/** Contrasena encriptada del perfil para sincronizarse con la aplicacion Web*/
	private String enPass;
	/** Nombre del perfil*/
	private String name;
	/** Fecha de nacimiento del usuario */
	private Date birthday;
	/** Altura del usuario */
	private Short height;
	/** Peso del usuario */
	private Short weight;
	/** Genero del usuario */
	private Gender gender;
	/** Longitud del paso del usuario */
	private Short stepLength;
	/** VO2 Maximo */
	private Float vo2Max;
	/** Url de la imagen del usuario */
	private String imagePath;
	/** Coordenada que representa el inicio habitual de los ejercicios */
	private MapPosition mapPosition;
	/** Estado de la sincronizacion del usuario contra el servicio Web */
	private HttpStatusType syncStatus;
	
	/**
	 * Constructor vacio
	 */
	public Profile() {}
	
	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the guid
	 */
	public String getGuid() {
		return guid;
	}

	/**
	 * @param guid the guid to set
	 */
	public void setGuid(String guid) {
		this.guid = guid;
	}

	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * @return the enPass
	 */
	public String getEnPass() {
		return enPass;
	}

	/**
	 * @param enPass the enPass to set
	 */
	public void setEnPass(String enPass) {
		this.enPass = enPass;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the birthday
	 */
	public Date getBirthday() {
		return birthday;
	}
	
	/**
	 * 
	 * @return the birthday in text format
	 */
	public String getTextBirthday() {
		return DateUtils.getDateFormatter().format(birthday);
	}

	/**
	 * @param birthday the birthday to set
	 */
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	/**
	 * @return the height
	 */
	public Short getHeight() {
		return height;
	}

	/**
	 * @param height the height to set
	 */
	public void setHeight(Short height) {
		this.height = height;
	}

	/**
	 * @return the weight
	 */
	public Short getWeight() {
		return weight;
	}

	/**
	 * @param weight the weight to set
	 */
	public void setWeight(Short weight) {
		this.weight = weight;
	}

	/**
	 * @return the gender
	 */
	public Gender getGender() {
		return gender;
	}

	/**
	 * @param gender the gender to set
	 */
	public void setGender(Gender gender) {
		this.gender = gender;
	}

	/**
	 * @return the stepLength
	 */
	public Short getStepLength() {
		return stepLength;
	}

	/**
	 * @param stepLength the stepLength to set
	 */
	public void setStepLength(Short stepLength) {
		this.stepLength = stepLength;
	}

	/**
	 * @return the vo2Max
	 */
	public Float getVo2Max() {
		return vo2Max;
	}

	/**
	 * @param vo2Max the vo2Max to set
	 */
	public void setVo2Max(Float vo2Max) {
		this.vo2Max = vo2Max;
	}

	/**
	 * @return the imagePath
	 */
	public String getImagePath() {
		return imagePath;
	}

	/**
	 * @param imagePath the imagePath to set
	 */
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	
	/**
	 * @return the mapPosition
	 */
	public MapPosition getMapPosition() {
		return mapPosition;
	}

	/**
	 * @param mapPosition the mapPosition to set
	 */
	public void setMapPosition(MapPosition mapPosition) {
		this.mapPosition = mapPosition;
	}

	/**
	 * @return the syncStatus
	 */
	public HttpStatusType getSyncStatus() {
		return syncStatus;
	}

	/**
	 * @param syncStatus the syncStatus to set
	 */
	public void setSyncStatus(HttpStatusType syncStatus) {
		this.syncStatus = syncStatus;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Profile [id=" + id + ", guid=" + guid + ", user=" + user
				+ ", enPass=" + enPass + ", name=" + name + ", birthday="
				+ birthday + ", height=" + height + ", weight=" + weight
				+ ", gender=" + gender + ", stepLength=" + stepLength
				+ ", vo2Max=" + vo2Max + ", imagePath=" + imagePath
				+ ", mapPosition=" + mapPosition + ", syncStatus=" + syncStatus
				+ "]";
	}
	
	/**
	 * Metodo que actualiza los valores del perfil con la informacion de la respuesta de la autenticacion
	 * @param autenticationResponseWS
	 */
	public void setAutenticationResponse(AutenticationResponseWS autenticationResponseWS) {
		this.setGuid(autenticationResponseWS.getIdUsuario());
		this.setName(autenticationResponseWS.getNombre());
		try {
			this.setBirthday(DateUtils.getWebServiceDateFormatter().parse(autenticationResponseWS.getFechaNacimiento()));
		} catch (ParseException e) {}
		this.setWeight(autenticationResponseWS.getPeso().shortValue());
		this.setHeight(autenticationResponseWS.getAltura().shortValue());
		this.setStepLength(autenticationResponseWS.getPaso().shortValue());
		this.setGender(Gender.valueOf(autenticationResponseWS.getGenero()));
	}
	
	/**
	 * Metodo que obtiene un objeto <code>RegistrationRequestWS</code> a partir de los datos
	 * del objeto actual
	 * @return
	 */
	public RegistrationRequestWS getRegistrationRequest() {
		RegistrationRequestWS request = new RegistrationRequestWS();
		request.setAltura(this.getHeight().floatValue());
		request.setFechaNacimiento(DateUtils.getWebServiceDateFormatter().format(this.getBirthday()));
		request.setGenero(this.getGender().getId());
		request.setNombre(this.getName());
		request.setPaso(this.getStepLength().intValue());
		request.setPeso(this.getWeight().floatValue());
		return request;
	}
	
	/**
	 * Metodo que obtiene un objeto <code>RegistrationRequestWS</code> a partir de los datos
	 * del objeto actual
	 * @return
	 */
	public EditRequestWS getEditRequest() {
		EditRequestWS request = new EditRequestWS();
		request.setAltura(this.getHeight().floatValue());
		request.setFechaNacimiento(DateUtils.getWebServiceDateFormatter().format(this.getBirthday()));
		request.setGenero(this.getGender().getId());
		request.setNombre(this.getName());
		request.setPaso(this.getStepLength().intValue());
		request.setPeso(this.getWeight().floatValue());
		return request;
	}
}
