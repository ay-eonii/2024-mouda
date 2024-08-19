import * as S from './MainPage.style';

import { Fragment, useMemo, useState } from 'react';
import MoimTabBar, { MainPageTab } from '@_components/MoimTabBar/MoimTabBar';
import OptionsPanel, {
  OptionsPanelOption,
} from '@_components/OptionsPanel/OptionsPanel';

import HomeLayout from '@_layouts/HomeLayout.tsx/HomeLayout';
import HomeMainContent from '@_components/HomeMainContent/HomeMainContent';
import NavigationBar from '@_components/NavigationBar/NavigationBar';
import NavigationBarWrapper from '@_layouts/components/NavigationBarWrapper/NavigationBarWrapper';
import Notification from '@_common/assets/notification.svg';
import PlusButton from '@_components/PlusButton/PlusButton';
import ROUTES from '@_constants/routes';
import SolidArrow from '@_components/Icons/SolidArrow';
import useMyDarakbangs from '@_hooks/queries/useMyDarakbang';
import useMyRoleInDarakbang from '@_hooks/queries/useMyDarakbangRole';
import { useNavigate } from 'react-router-dom';

export default function MainPage() {
  const navigate = useNavigate();

  const [currentTab, setCurrentTab] = useState<MainPageTab>('모임목록');
  const [isDarakbangMenuOpened, setIsDarakbangMenuOpened] = useState(false);

  const { myDarakbangs, isLoading: isMyDarakbangLoading } = useMyDarakbangs();

  const nowDarakbangName = '소파밥';
  const nowDarakbangId = 1;
  const { myRoleInDarakbang: myRoleInNowDarakbang } =
    useMyRoleInDarakbang(nowDarakbangId);

  const handleTabClick = (tab: MainPageTab) => {
    setCurrentTab(tab);
  };

  const handleNotification = () => {
    navigate(ROUTES.notification);
  };

  const darakbangMenuOption = useMemo(() => {
    if (isMyDarakbangLoading) return [];
    const options: OptionsPanelOption[] =
      myDarakbangs?.map(({ name, darakbangId }) => {
        return {
          onClick: () => navigate(ROUTES.main),
          description:
            name + (darakbangId === nowDarakbangId ? '(현재 다락방)' : ''),
        };
      }) || [];

    options.push({
      onClick: () => navigate(ROUTES.darakbangEntrance),
      description: '다른 다락방 들어가기',
      hasTopBorder: true,
    });

    if (myRoleInNowDarakbang === 'MANAGER') {
      options.push({
        onClick: () => navigate(ROUTES.darakbangManagement),
        description: '다락방 관리하기',
        hasTopBorder: true,
      });
    }
    return options;
  }, [isMyDarakbangLoading, myDarakbangs, myRoleInNowDarakbang, navigate]);

  const darakbangMenu = useMemo(() => {
    return (
      <OptionsPanel
        options={darakbangMenuOption}
        onClose={() => setIsDarakbangMenuOpened(false)}
        onAfterSelect={() => setIsDarakbangMenuOpened(false)}
        movedHeight="5rem"
        movedWidth="3rem"
        width="80%"
      />
    );
  }, [darakbangMenuOption]);
  return (
    <Fragment>
      <HomeLayout>
        <HomeLayout.Header>
          <HomeLayout.Header.Top>
            <HomeLayout.Header.Top.Left>
              <div css={S.headerLeft}>
                {nowDarakbangName}
                <SolidArrow
                  direction={isDarakbangMenuOpened ? 'up' : 'down'}
                  onClick={(e) => {
                    e.stopPropagation();
                    setIsDarakbangMenuOpened(!isDarakbangMenuOpened);
                  }}
                  width="15"
                  height="15"
                />
              </div>
            </HomeLayout.Header.Top.Left>

            <HomeLayout.Header.Top.Right>
              <button css={S.headerButton} onClick={handleNotification}>
                <Notification />
              </button>
            </HomeLayout.Header.Top.Right>
          </HomeLayout.Header.Top>
          {isDarakbangMenuOpened && darakbangMenu}
          <MoimTabBar currentTab={currentTab} onTabClick={handleTabClick} />
        </HomeLayout.Header>

        <HomeLayout.Main>
          <HomeMainContent currentTab={currentTab} />
        </HomeLayout.Main>

        <HomeLayout.HomeFixedButtonWrapper>
          <PlusButton onClick={() => navigate(ROUTES.addMoim)} />
        </HomeLayout.HomeFixedButtonWrapper>
      </HomeLayout>

      <NavigationBarWrapper>
        <NavigationBar />
      </NavigationBarWrapper>
    </Fragment>
  );
}
