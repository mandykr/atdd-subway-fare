package nextstep.auth.authorization;

import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.auth.userdetails.UserDetailsService;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

public class AuthenticationPrincipalArgumentResolver implements HandlerMethodArgumentResolver {
    private final UserDetailsService userDetailsService;

    public AuthenticationPrincipalArgumentResolver(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }


    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticationPrincipal.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        boolean required = Objects.requireNonNull(
                parameter.getParameterAnnotation(AuthenticationPrincipal.class)).required();

        if (!required && Objects.isNull(authentication)) {
            return userDetailsService.getNonLoginMember();
        }
        if (authentication.getPrincipal() instanceof Map) {
            return extractPrincipal(parameter, authentication);
        }

        return authentication.getPrincipal();
    }

    private Object extractPrincipal(MethodParameter parameter, Authentication authentication) {
        try {
            Map<String, String> principal = (Map) authentication.getPrincipal();

            Object[] params = Arrays.stream(parameter.getParameterType().getDeclaredFields())
                    .map(it -> toObject(it.getType(), principal.get(it.getName())))
                    .toArray();

            return parameter.getParameterType().getConstructors()[0].newInstance(params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object toObject(Class clazz, String value) {
        if (Boolean.class == clazz) return Boolean.parseBoolean(value);
        if (Byte.class == clazz) return Byte.parseByte(value);
        if (Short.class == clazz) return Short.parseShort(value);
        if (Integer.class == clazz) return Integer.parseInt(value);
        if (Long.class == clazz) return Long.parseLong(value);
        if (Float.class == clazz) return Float.parseFloat(value);
        if (Double.class == clazz) return Double.parseDouble(value);
        return value;
    }
}
