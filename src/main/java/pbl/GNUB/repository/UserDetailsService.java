package pbl.GNUB.repository;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserDetailsService {
    UserDetails loadByUsername(String email) throws UsernameNotFoundException;
}
