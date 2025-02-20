package nextstep.auth.userdetails;

public interface UserDetailsService {
    UserDetails loadUserByUsername(String principal);

    UserDetails getNonLoginMember();
}
