import { IEmployer, NewEmployer } from './employer.model';

export const sampleWithRequiredData: IEmployer = {
  id: 29581,
  label: 'ack',
};

export const sampleWithPartialData: IEmployer = {
  id: 10289,
  label: 'boo woefully about',
};

export const sampleWithFullData: IEmployer = {
  id: 5085,
  label: 'ultimately',
  linkedinUrl: 'happen parallel grammar',
  score: 31209.16,
};

export const sampleWithNewData: NewEmployer = {
  label: 'yum semicolon bah',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
