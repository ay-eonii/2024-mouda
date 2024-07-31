import type { Meta, StoryObj } from '@storybook/react';
import CommentCard from './CommentCard';

const meta = {
  component: CommentCard,
  title: 'Components/CommentCard',
} satisfies Meta<typeof CommentCard>;

export default meta;

type Story = StoryObj<typeof meta>;

export const Default: Story = {
  args: {
    comment: {
      id: 0,
      nickname: 'nickname',
      content: 'content',
      dateTime: '2023-04-04 14:00',
      child: [
        {
          id: 0,
          nickname: 'nickname',
          content: 'content',
          dateTime: '2023-04-04 14:00',
          child: [],
        },
      ],
    },
  },
  render: (args) => <CommentCard {...args} />,
};
