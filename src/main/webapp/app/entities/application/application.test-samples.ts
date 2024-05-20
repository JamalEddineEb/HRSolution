import dayjs from 'dayjs/esm';

import { IApplication, NewApplication } from './application.model';

export const sampleWithRequiredData: IApplication = {
  id: 18169,
  title: 'to yowza',
  description: 'slow',
  numberOfCandidates: 13925,
  paymentAmount: 9974.61,
  recruiterIncomeRate: 7424.62,
  candidateIncomeRate: 25245.74,
  status: 'NEW',
};

export const sampleWithPartialData: IApplication = {
  id: 29405,
  title: 'where up',
  description: 'lest phew realistic',
  numberOfCandidates: 21594,
  paymentAmount: 7886.8,
  recruiterIncomeRate: 22162.64,
  candidateIncomeRate: 3210.68,
  status: 'NEW',
};

export const sampleWithFullData: IApplication = {
  id: 17254,
  title: 'apud sack',
  description: 'provided midst',
  numberOfCandidates: 9292,
  paymentAmount: 998.71,
  recruiterIncomeRate: 6593.01,
  candidateIncomeRate: 21142.46,
  deadline: dayjs('2024-05-17'),
  status: 'ABORTED',
  createdAt: dayjs('2024-05-17T05:52'),
  openedAt: dayjs('2024-05-17T11:02'),
  closedAt: dayjs('2024-05-17T06:23'),
  doneAt: dayjs('2024-05-17T02:13'),
};

export const sampleWithNewData: NewApplication = {
  title: 'amongst',
  description: 'border unhitch snip',
  numberOfCandidates: 13718,
  paymentAmount: 29062.9,
  recruiterIncomeRate: 7407.65,
  candidateIncomeRate: 17044.2,
  status: 'CLOSED',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
