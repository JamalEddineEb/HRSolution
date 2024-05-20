import { IResume, NewResume } from './resume.model';

export const sampleWithRequiredData: IResume = {
  id: 31318,
  name: 'vice forceful',
  document: '../fake-data/blob/hipster.png',
  documentContentType: 'unknown',
};

export const sampleWithPartialData: IResume = {
  id: 13388,
  name: 'productivity who',
  document: '../fake-data/blob/hipster.png',
  documentContentType: 'unknown',
};

export const sampleWithFullData: IResume = {
  id: 11822,
  name: 'reservoir vastly',
  document: '../fake-data/blob/hipster.png',
  documentContentType: 'unknown',
};

export const sampleWithNewData: NewResume = {
  name: 'helpfully amidst',
  document: '../fake-data/blob/hipster.png',
  documentContentType: 'unknown',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
