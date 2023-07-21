package renteriaarce.infraccion.repository;

import org.springframework.data.domain.Pageable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import renteriaarce.infraccion.entity.Infraccion;


@Repository
public interface InfraccionRepository extends JpaRepository<Infraccion, Integer> {
	List <Infraccion> findByInfraccionContaining(String infraccion, Pageable page);
	Infraccion findByInfraccion(String infraccion);
	//List <Articulo> findByPrecioContainnig(double precio, Pageable page);
	//List <Articulo> findByPrecio(double precio, Pageable page);
	
}
