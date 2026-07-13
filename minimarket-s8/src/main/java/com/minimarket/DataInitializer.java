package com.minimarket;

import com.minimarket.entity.*;
import com.minimarket.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Set;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(ProductoRepository productoRepository,
                                   CategoriaRepository categoriaRepository,
                                   UsuarioRepository usuarioRepository,
                                   RolRepository rolRepository,
                                   PasswordEncoder passwordEncoder) {
        return args -> {
            // 1. Crear roles básicos si no existen
            if (rolRepository.count() == 0) {
                Rol adminRol = new Rol();
                adminRol.setNombre("ROLE_ADMIN");
                rolRepository.save(adminRol);

                Rol userRol = new Rol();
                userRol.setNombre("ROLE_USER");
                rolRepository.save(userRol);
            }

            // 2. Crear usuarios si no existen
            if (usuarioRepository.count() == 0) {
                // Obtenemos los roles de forma segura manejando el Optional
                Rol adminRol = rolRepository.findByNombre("ROLE_ADMIN")
                        .orElseThrow(() -> new RuntimeException("Error: Rol ROLE_ADMIN no encontrado"));

                Rol userRol = rolRepository.findByNombre("ROLE_USER")
                        .orElseThrow(() -> new RuntimeException("Error: Rol ROLE_USER no encontrado"));

                Usuario admin = new Usuario();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("password"));
                admin.setRoles(Set.of(adminRol));
                usuarioRepository.save(admin);

                Usuario user = new Usuario();
                user.setUsername("user");
                user.setPassword(passwordEncoder.encode("password"));
                user.setRoles(Set.of(userRol));
                usuarioRepository.save(user);
            }

            // 3. Crear datos de negocio
            if (categoriaRepository.count() == 0) {
                Categoria cat = new Categoria();
                cat.setNombre("Lácteos");
                categoriaRepository.save(cat);

                Producto p = new Producto();
                p.setNombre("Leche Entera");
                p.setPrecio(1200.0);
                p.setStock(50);
                p.setCategoria(cat);
                productoRepository.save(p);
            }
        };
    }
}