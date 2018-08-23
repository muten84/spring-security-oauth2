package it.eng.areas.sdoordinari.ordinarigto.converter.impl;

import it.eng.areas.sdoordinari.ordinarigto.converter.ConverterService;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

@Component
public class ConverterServiceImpl implements ConverterService {

	public ConverterServiceImpl() {
	}

//	@Autowired 
//	@Qualifier("tipologiaTrasporto") 
	@Resource(name="tipologiaTrasporto")
	public  Map<String, String> tipologiaTrasporto;
	
//	@Autowired 
//	@Qualifier("modalitaTrasporto") 
	@Resource(name="modalitaTrasporto")
	public  Map<String, String> modalitaTrasporto;
	
	@Resource(name="motivoTrasportoIntrah")
	public  Map<String, String> motivoTrasportoIntrah;
	
	@Resource(name="motivoTrasportoInterh")
	public  Map<String, String> motivoTrasportoInterh;

	@Resource(name="accompagnamento")
	public  Map<String, String> accompagnamento;

	@Resource(name="attrezzaturaAmbulanza")
	public  Map<String, String> attrezzaturaAmbulanza;
 	 
	@Resource(name="attrezzaturaASeguitoPaz")
	public  Map<String, String> attrezzaturaASeguitoPaz;
	
 	 
	@Resource(name="nonNoto")
	public  Map<String, String> nonNoto;
 	
	
	public Map<String, String> getnonNoto() {
		return nonNoto;
	}

	public void setnonNoto(Map<String, String> nonNoto) {
		this.nonNoto = nonNoto;
	}

	public Map<String, String> getattrezzaturaAmbulanza() {
		return attrezzaturaAmbulanza;
	}

	public void setattrezzaturaAmbulanza(Map<String, String> attrezzaturaAmbulanza) {
		this.attrezzaturaAmbulanza = attrezzaturaAmbulanza;
	}

	public Map<String, String> getaccompagnamento() {
		return accompagnamento;
	}

	public void setaccompagnamento(Map<String, String> accompagnamento) {
		this.accompagnamento = accompagnamento;
	}




	public Map<String, String> getmotivoTrasportoInterh() {
		return motivoTrasportoInterh;
	}




	public void setmotivoTrasportoInterh(Map<String, String> motivoTrasportoInterh) {
		this.motivoTrasportoInterh = motivoTrasportoInterh;
	}


	
	public Map<String, String> getmotivoTrasportoIntrah() {
		return motivoTrasportoIntrah;
	}



	public void setmotivoTrasportoIntrah(Map<String, String> motivoTrasportoIntrah) {
		this.motivoTrasportoIntrah = motivoTrasportoIntrah;
	}



	public Map<String, String> gettipologiaTrasporto() {
		return tipologiaTrasporto;
	}

	public void settipologiaTrasporto(Map<String, String> tipologiaTrasporto) {
		this.tipologiaTrasporto = tipologiaTrasporto;
	}

	public Map<String, String> getmodalitaTrasporto() {
		return modalitaTrasporto;
	}

	public void setmodalitaTrasporto(Map<String, String> modalitaTrasporto) {
		this.modalitaTrasporto = modalitaTrasporto;
	}

	
	
	public Map<String, String> getattrezzaturaASeguitoPaz() {
		return attrezzaturaASeguitoPaz;
	}

	public void setattrezzaturaASeguitoPaz(
			Map<String, String> attrezzaturaASeguitoPaz) {
		this.attrezzaturaASeguitoPaz = attrezzaturaASeguitoPaz;
	}

	@Override
	public Map<String, String> getMapFromString(String parameter, Object o ) {

		 try {
		Class<?> thisClass = Class.forName(this.getClass().getName());
		Method method = thisClass.getMethod("get"+parameter);
		  Map<String, String> map =(Map<String, String>) method.invoke(o);
		  return map;
		
		 } catch (NoSuchMethodException | SecurityException | IllegalArgumentException | IllegalAccessException | ClassNotFoundException | InvocationTargetException  e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
		 
		return null;
	
	}
}