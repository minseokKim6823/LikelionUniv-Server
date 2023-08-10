package likelion.univ.adminUser.usecase;

import likelion.univ.adminUser.dto.response.UserInfoResponseDto;
import likelion.univ.annotation.UseCase;
import likelion.univ.domain.user.adaptor.UserAdaptor;
import likelion.univ.domain.user.entity.User;
import likelion.univ.domain.user.service.UserDomainService;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class DeleteUserUseCase {

    private final UserAdaptor userAdaptor;
    private final UserDomainService userDomainService;

    public UserInfoResponseDto deleteUser(Long id){
        User user = userAdaptor.findById(id);
        userAdaptor.deleteUser(user);
        return UserInfoResponseDto.of(user);
    }
}
