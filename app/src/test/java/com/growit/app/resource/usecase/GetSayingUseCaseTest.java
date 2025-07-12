package com.growit.app.resource.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.growit.app.fake.resource.SayingFixture;
import com.growit.app.resource.domain.saying.Saying;
import com.growit.app.resource.domain.saying.repository.SayingRepository;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetSayingUseCaseTest {

  @Mock private SayingRepository sayingRepository;

  @InjectMocks private GetSayingUseCase useCase;

  @Test
  void givenNoSayings_whenExecute_thenReturnDefaultSaying() {
    // given
    when(sayingRepository.findAll()).thenReturn(Collections.emptyList());

    // when
    Saying result = useCase.execute();

    // then
    assertThat(result.getId()).isEqualTo("tempId");
  }

  @Test
  void givenSayingsExist_whenExecute_thenReturnOneOfTheSayings() {
    // given
    List<Saying> sayings =
        Arrays.asList(
            SayingFixture.customSaying("1", "문장1", "작성자1"),
            SayingFixture.customSaying("2", "문장2", "작성자2"));
    when(sayingRepository.findAll()).thenReturn(sayings);

    // when
    Saying result = useCase.execute();

    // then
    assertThat(sayings).contains(result);
  }
}
