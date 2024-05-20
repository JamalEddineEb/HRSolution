import { IAdmin, NewAdmin } from './admin.model';

export const sampleWithRequiredData: IAdmin = {
  id: 17606,
  systemName: 'especially excepting',
};

export const sampleWithPartialData: IAdmin = {
  id: 16308,
  systemName: 'regularly whereas er',
};

export const sampleWithFullData: IAdmin = {
  id: 9646,
  systemName: 'pickle',
};

export const sampleWithNewData: NewAdmin = {
  systemName: 'condor phew',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
