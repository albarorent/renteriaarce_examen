package renteriaarce.infraccion.soap;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import renteriaarce.infraccion.entity.Infraccion;
import renteriaarce.infraccion.service.AddInfraccionRequest;
import renteriaarce.infraccion.service.AddInfraccionResponse;
import renteriaarce.infraccion.service.DeleteInfraccionRequest;
import renteriaarce.infraccion.service.DeleteInfraccionResponse;
import renteriaarce.infraccion.service.GetAllInfraccionRequest;
import renteriaarce.infraccion.service.GetAllInfraccionResponse;
import renteriaarce.infraccion.service.GetInfraccionRequest;
import renteriaarce.infraccion.service.GetInfraccionResponse;
import renteriaarce.infraccion.service.InfraccionDetalle;
import renteriaarce.infraccion.service.InfraccionService;
import renteriaarce.infraccion.service.ServiceStatus;
import renteriaarce.infraccion.service.UpdateInfraccionRequest;
import renteriaarce.infraccion.service.UpdateInfraccionResponse;



@Endpoint
public class InfraccionEndPoint {
	@Autowired
	private InfraccionService service;
	
	@PayloadRoot(namespace = "http://uss.edu.pe/infraccionservice", localPart = "GetInfraccionResponse")
	@ResponsePayload
	public GetAllInfraccionRequest findAll (@RequestPayload GetInfraccionResponse request) {
		GetInfraccionResponse response = new GetInfraccionResponse();
		
		Pageable page = PageRequest.of(request.getOffset(), request.getLimit());
		List<Infraccion> infraccions;
		if(request.getTexto()==null) {
			infraccions = service.findAll(page);
		}else {
			infraccions=service.infraccion(request.getTexto(), page);
		}
		
		List<InfraccionDetalle> infraccionResponse=new ArrayList<>();
		for (int i = 0; i < infraccions.size(); i++) {
			InfraccionDetalle ob = new InfraccionDetalle();
		     BeanUtils.copyProperties(infraccions.get(i), ob);
		     infraccionResponse.add(ob);    
		}
		response.get().addAll(infraccionResponse);
		return response;
	}
	
	@PayloadRoot(namespace = "http://uss.edu.pe/articuloservice", localPart = "GetInfraccionRequest")
	@ResponsePayload
	public GetInfraccionResponse findById (@RequestPayload GetInfraccionRequest request) {
		GetInfraccionResponse response = new GetInfraccionResponse();
		Infraccion entity=service.findById(request.getId());
		InfraccionDetalle infraccion=new InfraccionDetalle();
		infraccion.setId(entity.getId());
		infraccion.setDni(entity.getDNI());
		infraccion.setFalta(entity.getInfraccion());
		infraccion.setInfraccion(entity.getInfraccion());
		infraccion.setDescripcion(entity.getDescripcion());
		response.setInfraccionDetalle(infraccion);		
		return response;
	}
	
	@PayloadRoot(namespace = "http://uss.edu.pe/articuloservice", localPart = "AddArticuloRequest")
	@ResponsePayload
	public AddInfraccionResponse create (@RequestPayload AddInfraccionRequest request) {
		ServiceStatus serviceStatus=new ServiceStatus();
		AddInfraccionResponse response = new AddInfraccionResponse();
		Infraccion entity = new Infraccion();
		entity.setDNI(request.getDni());
		entity.setFalta(request.getFalta());
		entity.setInfraccion(request.getInfraccion());
		entity.setDescripcion(request.getDescripcion());

		entity=service.save(entity);
		if(entity!=null) {
			InfraccionDetalle infraccion=new InfraccionDetalle();
			BeanUtils.copyProperties(entity, infraccion);
			response.setInfraccionDetalle(infraccion);
			serviceStatus.setStatusCode("SUCCESS");
			serviceStatus.setMessage("Content Added Successfully");
			response.setServiceStatus(serviceStatus);
		}else {
			serviceStatus.setStatusCode("CONFLICT");
			serviceStatus.setMessage("Content Already Available");
			response.setServiceStatus(serviceStatus);
		}
		return response;
	}
	@PayloadRoot(namespace = "http://uss.edu.pe/articuloservice", localPart = "UpdateInfraccionRequest")
	@ResponsePayload
	public UpdateInfraccionResponse update (@RequestPayload UpdateInfraccionRequest request) {
		ServiceStatus serviceStatus = new ServiceStatus();
		UpdateInfraccionResponse response= new UpdateInfraccionResponse();
		Infraccion entity = service.findById(request.getId());
		entity.setDNI(request.getDni());
		entity.setFalta(request.getFalta());
		entity.setInfraccion(request.getInfraccion());
		entity.setDescripcion(request.getDescripcion());

		entity=service.update(entity);		
		if (entity!=null) {
			InfraccionDetalle infraccion= new InfraccionDetalle();
			BeanUtils.copyProperties(entity, infraccion);
			response.setInfraccionDetalle(infraccion);
			serviceStatus.setStatusCode("SUCCESS");
     	    serviceStatus.setMessage("Content Updated Successfully");
     	    response.setServiceStatus(serviceStatus);			
		}else {
			serviceStatus.setStatusCode("CONFLICT");
	     	serviceStatus.setMessage("Content Not Updated");
	     	response.setServiceStatus(serviceStatus);
		}
		return response;
	}
	
	@PayloadRoot(namespace = "http://uss.edu.pe/articuloservice", localPart = "DeleteInfraccionRequest")
	@ResponsePayload
	public DeleteInfraccionResponse create (@RequestPayload DeleteInfraccionRequest request) {
		ServiceStatus serviceStatus=new ServiceStatus();
		DeleteInfraccionResponse response = new DeleteInfraccionResponse();
		boolean resp=service.delete(request.getId());
		if(resp) {
			serviceStatus.setStatusCode("SUCCESS");
			serviceStatus.setMessage("Content Deleted Successfully");
			response.setServiceStatus(serviceStatus);
		}else {
			serviceStatus.setStatusCode("CONFLICT");
			serviceStatus.setMessage("Content Not Deleted");
			response.setServiceStatus(serviceStatus);
		}
		return response;
	}
	
}
