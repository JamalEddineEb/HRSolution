import { IResume, NewResume } from './resume.model';

export const sampleWithRequiredData: IResume = {
  id: 4478,
  cv: '../fake-data/blob/hipster.png',
  cvContentType: 'unknown',
};

export const sampleWithPartialData: IResume = {
  id: 31318,
  cv: '../fake-data/blob/hipster.png',
  cvContentType: 'unknown',
};

export const sampleWithFullData: IResume = {
  id: 21737,
  cv: '../fake-data/blob/hipster.png',
  cvContentType: 'unknown',
};

export const sampleWithNewData: NewResume = {
  cv: '../fake-data/blob/hipster.png',
  cvContentType: 'unknown',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
