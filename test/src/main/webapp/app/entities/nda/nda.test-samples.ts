import dayjs from 'dayjs/esm';

import { INDA, NewNDA } from './nda.model';

export const sampleWithRequiredData: INDA = {
  id: 29067,
  document: '../fake-data/blob/hipster.png',
  documentContentType: 'unknown',
  status: 'REJECTED',
  period: dayjs('2024-05-20'),
};

export const sampleWithPartialData: INDA = {
  id: 27875,
  document: '../fake-data/blob/hipster.png',
  documentContentType: 'unknown',
  status: 'REJECTED',
  period: dayjs('2024-05-19'),
};

export const sampleWithFullData: INDA = {
  id: 20355,
  document: '../fake-data/blob/hipster.png',
  documentContentType: 'unknown',
  status: 'SIGNED',
  period: dayjs('2024-05-20'),
};

export const sampleWithNewData: NewNDA = {
  document: '../fake-data/blob/hipster.png',
  documentContentType: 'unknown',
  status: 'PROCESSING',
  period: dayjs('2024-05-19'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
