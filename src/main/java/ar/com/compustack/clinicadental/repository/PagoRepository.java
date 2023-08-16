package ar.com.compustack.clinicadental.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ar.com.compustack.clinicadental.model.Pago;


public interface PagoRepository extends JpaRepository<Pago, Integer> 
{
    @Query(value = "SELECT * FROM pagos WHERE fechaPago BETWEEN DATE_SUB(NOW(), INTERVAL 30 DAY) AND NOW() ORDER BY fechaPago DESC", nativeQuery = true)
    List<Pago> getPagosFromLast30Days();   
}
