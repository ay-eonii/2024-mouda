import { ChangeEvent, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';

import BackLogo from '@_common/assets/back.svg';
import Button from '@_components/Button/Button';
import InformationLayout from '@_layouts/InformationLayout/InformationLayout';
import LabeledInput from '@_components/Input/MoimInput';
import MoimDescription from '@_components/MoimDescription/MoimDescription';
import MoimInformation from '@_components/MoimInformation/MoimInformation';
import MoimSummary from '@_components/MoimSummary/MoimSummary';
import ROUTES from '@_constants/routes';
import useJoinMoim from '@_hooks/mutaions/useJoinMoim';
import useMoim from '@_hooks/queries/useMoim';

export default function MoimDetailPage() {
  const navigate = useNavigate();
  const params = useParams();
  const [nickName, setNickName] = useState('');

  const moimId = Number(params.moimId);

  const { moim, isLoading } = useMoim(moimId);
  const { mutate } = useJoinMoim(() => {
    navigate(ROUTES.participationComplete);
  });

  if (isLoading) {
    return <div>Loading...</div>;
  }
  if (!moim) {
    return <div>No data found</div>;
  }
  return (
    <InformationLayout>
      <InformationLayout.Header>
        <InformationLayout.Header.Left>
          <div onClick={() => navigate(-1)}>
            <BackLogo />
          </div>
        </InformationLayout.Header.Left>
      </InformationLayout.Header>
      <InformationLayout.ContentContainer>
        <MoimSummary moimInfo={moim} />
        <MoimInformation moimInfo={moim} />

        {moim.description && <MoimDescription description={moim.description} />}

        <LabeledInput
          title="참가자 이름"
          required
          onChange={(e: ChangeEvent<HTMLInputElement>) =>
            setNickName(e.target.value)
          }
        />
      </InformationLayout.ContentContainer>
      <InformationLayout.BottomButtonWrapper>
        <Button
          shape="bar"
          disabled={nickName === ''}
          onClick={() => mutate({ moimId, nickName })}
        >
          참여하기
        </Button>
      </InformationLayout.BottomButtonWrapper>
    </InformationLayout>
  );
}
