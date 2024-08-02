interface LabeledInputInfo {
  name: string;
  title: string;
  type: string;
  placeholder: string;
  required: boolean;
}

const MOIM_INPUT_INFOS: LabeledInputInfo[] = [
  {
    name: 'title',
    title: '제목',
    type: 'text',
    placeholder: '없음',
    required: true,
  },
  {
    name: 'date',
    title: '날짜',
    type: 'date',
    placeholder: '없음',
    required: true,
  },
  {
    name: 'time',
    title: '시간',
    type: 'time',
    placeholder: '없음',
    required: true,
  },
  {
    name: 'place',
    title: '장소',
    type: 'text',
    placeholder: '없음',
    required: true,
  },
  {
    name: 'maxPeople',
    title: '최대인원수',
    type: 'number',
    placeholder: '0명',
    required: true,
  },
  {
    name: 'description',
    title: '상세 내용 작성',
    type: 'textarea',
    placeholder: '어떤 모임인지 작성해주세요!',
    required: false,
  },
] as const;

export default MOIM_INPUT_INFOS;
