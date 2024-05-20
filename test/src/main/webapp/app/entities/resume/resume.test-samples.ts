import { IResume, NewResume } from './resume.model';

export const sampleWithRequiredData: IResume = {
  id: 31318,
  file: '../fake-data/blob/hipster.png',
  fileContentType: 'unknown',
  filename: 'vice forceful',
};

export const sampleWithPartialData: IResume = {
  id: 13388,
  file: '../fake-data/blob/hipster.png',
  fileContentType: 'unknown',
  filename: 'productivity who',
};

export const sampleWithFullData: IResume = {
  id: 11822,
  file: '../fake-data/blob/hipster.png',
  fileContentType: 'unknown',
  filename: 'reservoir vastly',
};

export const sampleWithNewData: NewResume = {
  file: '../fake-data/blob/hipster.png',
  fileContentType: 'unknown',
  filename: 'helpfully amidst',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
