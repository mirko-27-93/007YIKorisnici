package studiranje.ip.engine.controller;

/**
 * Ошшти оквир за објекте који садрже адресе сервиса. 
 * @author mirko
 * @version 1.0
 */
public interface GeneralService {
	public String getServiceAddress();
	public boolean existsServiceAddress(); 
	public void setServiceAddress(String serviceAddress);
}
