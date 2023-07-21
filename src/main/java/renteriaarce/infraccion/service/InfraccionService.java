package renteriaarce.infraccion.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import renteriaarce.infraccion.entity.Infraccion;
import renteriaarce.infraccion.repository.InfraccionRepository;


@Service
@Slf4j
public class InfraccionService {
	@Autowired
	private InfraccionRepository repository;
	
	@Transactional(readOnly = true)
	public List<Infraccion> findAll(Pageable page){
		try {
			return repository.findAll(page).toList();
			
		} catch(Exception e) {
			log.error(e.getMessage());
			return null;
		}
		
	}
	
	@Transactional(readOnly=true)
	public List<Infraccion> infraccion(String infraccion, Pageable page) {
		try {
			return repository.findByInfraccionContaining(infraccion,page);
		} catch (Exception e) {
			log.error(e.getMessage());
			return null;
		}
	}
	
	@Transactional(readOnly = true)
	public Infraccion findById(int id){
		try {
			Infraccion registro = repository.findById(id).orElseThrow();
			return registro;
			
		} catch(Exception e) {
			log.error(e.getMessage());
			return null;
		}
		
	}
	
	@Transactional
	public Infraccion save(Infraccion infraccion) {
		try {
			if(repository.findByInfraccion(infraccion.getInfraccion())!=null) {
				return null;
			}
			Infraccion nuevo=repository.save(infraccion);
			return nuevo;
		} catch (Exception e) {
			log.error(e.getMessage());
			return null;
		}
	}
	
	@Transactional(readOnly = true)
	public Infraccion update(Infraccion infraccion){
		try {
			Infraccion registro = repository.findById(infraccion.getId()).orElseThrow();
			Infraccion registroD = repository.findByInfraccion(infraccion.getInfraccion());
			
			if(registroD!=null && infraccion.getId()!=registroD.getId()) {
				return null;
			}
			registro.setInfraccion(infraccion.getDNI());
			registro.setInfraccion(infraccion.getInfraccion());
			registro.setInfraccion(infraccion.getDescripcion());
			registro.setInfraccion(infraccion.getFalta());

			repository.save(registro);
			return registro;
			
		} catch(Exception e) {
			log.error(e.getMessage());
			return null;
		}
		
	}
	
	@Transactional
	public boolean delete(int id) {
		try {
			Infraccion infraccion=repository.findById(id).orElseThrow();
			repository.delete(infraccion);
			return true;
		} catch (Exception e) {
			log.error(e.getMessage());
			return false;
		}
	}

}